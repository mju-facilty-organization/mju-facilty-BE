package com.example.rentalSystem.common.fixture;

import com.example.rentalSystem.domain.facility.entity.timeTable.TimeTable;
import java.time.LocalDate;
import java.time.LocalTime;

public class TimeTableFixture {

    protected static TimeTable createTimeTable() {
        return TimeTable.toEntity(
            null,
            LocalDate.now(),
            LocalTime.parse("11:00"),
            LocalTime.parse("15:00"));
    }
}
