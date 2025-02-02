package com.example.rentalSystem.domain.rentalhistory.implement;

import static com.example.rentalSystem.domain.facility.entity.RentalStatus.AVAILABLE;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.RentalStatus;
import com.example.rentalSystem.domain.facility.entity.TimeTable;
import com.example.rentalSystem.domain.facility.reposiotry.TimeTableRepository;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ErrorType;
import com.example.rentalSystem.global.response.SuccessType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RentalScheduler {

    final TimeTableRepository tableRepository;

    public void checkSchedule(
        Facility facility,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime) {

        LocalDate startDate = startDateTime.toLocalDate();
        LocalTime startTIme = startDateTime.toLocalTime();
        LocalTime endTime = endDateTime.toLocalTime();

        TimeTable timeTable = tableRepository.findByFacilityAndDate(facility,
                startDate)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));

        LinkedHashMap<LocalTime, RentalStatus> timeSlot = timeTable.getTimeSlot();

        while (startTIme.isBefore(endTime)) {
            if (!timeSlot.get(startTIme).equals(AVAILABLE)) {
                throw new CustomException(ErrorType.FAIL_RENTAL_REQUEST);
            }
            startTIme = startTIme.plusMinutes(30);
        }
    }
}
