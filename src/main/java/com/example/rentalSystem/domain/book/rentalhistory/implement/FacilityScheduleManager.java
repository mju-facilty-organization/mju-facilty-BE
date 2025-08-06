package com.example.rentalSystem.domain.book.rentalhistory.implement;

import com.example.rentalSystem.domain.book.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.book.schedule.entity.Schedule;
import com.example.rentalSystem.domain.book.schedule.implement.ScheduleReader;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FacilityScheduleManager {

    private final ScheduleReader scheduleReader;
    private final RentalHistoryImpl rentalHistoryImpl;

    public void checkAvailabilityAndReserve(
        Facility facility,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
    ) {
        LocalDate startDate = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();
        LocalTime endTime = endDateTime.toLocalTime();
        checkScheduleAvailability(
            facility.getId(), startDate,
            startTime, endTime
        );
        checkRentalHistoryAvailability(
            facility.getId(), startDate,
            startTime, endTime
        );
    }

    private void checkRentalHistoryAvailability(
        Long facilityId, LocalDate startDate,
        LocalTime startTime, LocalTime endTime
    ) {
        List<RentalHistory> rentalHistories = rentalHistoryImpl.getByFacilityIdAndDateAndBetweenTime(
            facilityId,
            startDate,
            startTime,
            endTime
        );
        if (!rentalHistories.isEmpty()) {
            throw new CustomException(ErrorType.RENTAL_HISTORY_CONFLICT);
        }
    }

    private void checkScheduleAvailability(Long facilityId,
        LocalDate startDate, LocalTime startTime, LocalTime endTime) {
        List<Schedule> schedules = scheduleReader.getByFacilityIdAndDateAndBetweenTime(
            facilityId,
            startDate,
            startTime,
            endTime
        );
        if (!schedules.isEmpty()) {
            throw new CustomException(ErrorType.SCHEDULE_CONFLICT);
        }
    }
}
