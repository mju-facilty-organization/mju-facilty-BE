package com.example.rentalSystem.domain.rentalhistory.implement;

import static com.example.rentalSystem.domain.facility.entity.timeTable.TimeStatus.AVAILABLE;
import static com.example.rentalSystem.domain.facility.entity.timeTable.TimeStatus.CURRENT;
import static com.example.rentalSystem.domain.facility.entity.timeTable.TimeStatus.RESERVED;
import static com.example.rentalSystem.domain.facility.entity.timeTable.TimeStatus.UNAVAILABLE;
import static com.example.rentalSystem.domain.facility.entity.timeTable.TimeStatus.WAITING;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.timeTable.TimeStatus;
import com.example.rentalSystem.domain.facility.entity.timeTable.TimeTable;
import com.example.rentalSystem.domain.facility.reposiotry.TimeTableRepository;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalApplicationResult;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FacilityScheduleManager {

    final TimeTableRepository tableRepository;

    public void checkAvailabilityAndReserve(
        Facility facility,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
    ) {
        LocalDate startDate = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();
        LocalTime endTime = endDateTime.toLocalTime();

        TimeTable timeTable = findTimeTable(facility, startDate);

        LinkedHashMap<LocalTime, TimeStatus> timeSlot = timeTable.getTimeSlot();

        validateTimeSlotAvailability(timeSlot, startTime, endTime);
        updateTimeSlotStatus(timeSlot, startTime, endTime, WAITING);
    }

    private void validateTimeSlotAvailability(LinkedHashMap<LocalTime, TimeStatus> timeSlot,
        LocalTime startTime, LocalTime endTime) {
        LocalTime time = startTime;
        while (time.isBefore(endTime)) {
            if (timeSlot.getOrDefault(time, UNAVAILABLE) != AVAILABLE) {
                throw new CustomException(ErrorType.FAIL_RENTAL_REQUEST);
            }
            time = time.plusMinutes(30);
        }
    }

    private void updateTimeSlotStatus(LinkedHashMap<LocalTime, TimeStatus> timeSlot,
        LocalTime startTime, LocalTime endTime,
        TimeStatus newStatus) {
        LocalTime time = startTime;
        while (time.isBefore(endTime)) {
            timeSlot.put(time, newStatus);
            time = time.plusMinutes(30);
        }
    }

    public void updateTimeStatus(Facility facility, LocalDateTime rentalStartDateTime,
        LocalDateTime rentalEndDateTime, RentalApplicationResult rentalApplicationResult) {

        LocalDate startDate = rentalStartDateTime.toLocalDate();
        LocalTime startTime = rentalStartDateTime.toLocalTime();
        LocalTime endTime = rentalEndDateTime.toLocalTime();

        TimeTable timeTable = findTimeTable(facility, startDate);

        switch (rentalApplicationResult) {
            case PROFESSOR_PERMITTED ->
                updateTimeSlotStatus(timeTable.getTimeSlot(), startTime, endTime, CURRENT);
            case PIC_PERMITTED ->
                updateTimeSlotStatus(timeTable.getTimeSlot(), startTime, endTime, RESERVED);
            case PIC_DENIED, PROFESSOR_DENIED ->
                updateTimeSlotStatus(timeTable.getTimeSlot(), startTime, endTime, AVAILABLE);
            case WAITING ->
                updateTimeSlotStatus(timeTable.getTimeSlot(), startTime, endTime, WAITING);
        }
    }

    private TimeTable findTimeTable(Facility facility, LocalDate startDate) {
        return tableRepository.findByFacilityAndDate(facility, startDate)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }
}
