package com.example.rentalSystem.domain.book.schedule.implement;

import com.example.rentalSystem.domain.book.schedule.entity.Schedule;
import com.example.rentalSystem.domain.book.schedule.repository.ScheduleRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleReader {

    private final ScheduleRepository scheduleRepository;

    public List<Schedule> getSchedulesInBoundary(
        Long facilityId,
        DayOfWeek dayOfWeek,
        LocalDate validStartDate,
        LocalDate validEndDate
    ) {
        return scheduleRepository.findConflictsBy(facilityId, dayOfWeek, validStartDate,
            validEndDate);
    }

    public List<Schedule> getByFacilityIdAndDateAndBetweenTime(Long facilityId, LocalDate startDate,
        LocalTime startTime, LocalTime endTime) {
        DayOfWeek dayOfWeek = startDate.getDayOfWeek();
        return scheduleRepository.findByFacilityIdAndDateAndBetweenTime(facilityId, dayOfWeek,
            startTime, endTime);
    }

    public List<Schedule> getByFacilityIdAndDate(Long facilityId, LocalDate localDate) {
        return scheduleRepository.findByFacilityIdAndDate(facilityId, localDate.getDayOfWeek(),
            localDate);
    }
}
