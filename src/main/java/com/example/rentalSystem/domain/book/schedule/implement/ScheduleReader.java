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

    public List<Schedule> getValidSchedulesForDayOfWeek(
        Long facilityId,
        DayOfWeek dayOfWeek,
        LocalDate validStartDate,
        LocalDate validEndDate
    ) {
        // 날짜사이의 특정 요일만 가져온다?
        return scheduleRepository.findValidSchedulesForDayOfWeek(facilityId, dayOfWeek,
            validStartDate,
            validEndDate);
    }

    public List<Schedule> getByFacilityIdAndDateAndBetweenTime(Long facilityId, LocalDate startDate,
        LocalTime startTime, LocalTime endTime) {
        DayOfWeek dayOfWeek = startDate.getDayOfWeek();
        return scheduleRepository.findByFacilityIdAndDateAndBetweenTime(facilityId, dayOfWeek,
            startTime, endTime);
    }

    public List<Schedule> getFacilityTodaySchedules(Long facilityId, LocalDate localDate) {

        // 데이터베이스에서 스케줄을 가져옵니다.
        List<Schedule> schedules = scheduleRepository.findValidSchedulesByFacilityIdAndDate(
            facilityId, localDate);

        // 이제 비즈니스 로직에서 요일을 확인하여 필터링합니다.
        DayOfWeek currentDay = localDate.getDayOfWeek();
        return schedules.stream()
            .filter(schedule -> schedule.getDayOfWeek() == currentDay)
            .toList();
    }
}
