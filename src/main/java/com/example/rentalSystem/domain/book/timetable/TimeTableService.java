package com.example.rentalSystem.domain.book.timetable;

import com.example.rentalSystem.domain.book.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.book.rentalhistory.implement.RentalHistoryImpl;
import com.example.rentalSystem.domain.book.schedule.entity.Schedule;
import com.example.rentalSystem.domain.book.schedule.implement.ScheduleReader;
import com.example.rentalSystem.domain.facility.entity.Facility;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimeTableService {

    private final ScheduleReader scheduleReader;
    private final RentalHistoryImpl rentalHistoryImpl;

    public TimeTable getTimeTable(Facility facility, LocalDate localDate) {

        TimeTable timeTable = TimeTable.createTimeTable(
            facility.getStartTime(), facility.getEndTime(), localDate
        );

        List<Schedule> schedules = scheduleReader.getByFacilityIdAndDate(facility.getId(),
            localDate);
        applySchedules(timeTable, schedules);

        List<RentalHistory> rentalHistories = rentalHistoryImpl.getByFacilityIdAndDate(
            facility.getId(), localDate);
        applyRentalHistory(timeTable, rentalHistories);

        return timeTable;
    }

    private void applyRentalHistory(TimeTable timeTable, List<RentalHistory> rentalHistories) {
        for (RentalHistory rh : rentalHistories) {
            LocalTime startTime = rh.getRentalStartTime();
            LocalTime endTime = rh.getRentalEndTime();
            TimeStatus status = rh.getRentalApplicationResult().getTimeStatus();
            LocalTime currentTime = startTime;
            while (currentTime.isBefore(endTime)) {
                timeTable.updateStatus(currentTime, status);
                currentTime = currentTime.plusMinutes(30);
            }
        }
    }

    private void applySchedules(TimeTable timeTable, List<Schedule> schedules) {
        for (Schedule schedule : schedules) {
            LocalTime startTime = schedule.getRentalStartTime();
            LocalTime endTime = schedule.getRentalEndTime();
            LocalTime currentTime = startTime;
            while (currentTime.isBefore(endTime)) {
                timeTable.updateStatus(currentTime, TimeStatus.UNAVAILABLE);
                currentTime = currentTime.plusMinutes(30);
            }
        }
    }
}
