package com.example.rentalSystem.domain.book.timetable;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class TimeTable {

    private final LinkedHashMap<LocalTime, TimeStatus> timeTable;

    private final LocalDate date;

    public static TimeTable createTimeTable(LocalTime startTime, LocalTime endTime,
        LocalDate date) {
        LinkedHashMap<LocalTime, TimeStatus> timeTable = new LinkedHashMap<>();
        while (startTime.isBefore(endTime)) {
            timeTable.put(startTime, TimeStatus.AVAILABLE);
            startTime = startTime.plusMinutes(30);
        }
        return TimeTable.builder()
            .date(date)
            .timeTable(timeTable)
            .build();
    }

    public void updateStatus(LocalTime time, TimeStatus status) {
        if (timeTable.containsKey(time)) {
            timeTable.put(time, status);
        }
    }
}
