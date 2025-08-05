package com.example.rentalSystem.domain.book.schedule.implement;

import com.example.rentalSystem.domain.book.schedule.entity.Schedule;
import com.example.rentalSystem.domain.book.schedule.repository.ScheduleRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
}
