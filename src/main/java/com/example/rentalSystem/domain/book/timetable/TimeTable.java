package com.example.rentalSystem.domain.book.timetable;


import com.example.rentalSystem.domain.book.dto.BookInfoResponse;
import com.example.rentalSystem.domain.book.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.book.schedule.entity.Schedule;
import com.example.rentalSystem.domain.facility.entity.type.ScheduleType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import lombok.Builder;

@Builder
@JsonPropertyOrder({"date", "timeTable"})
public record TimeTable(
    LinkedHashMap<LocalTime, TimeStatus> timeTable,
    LinkedHashMap<LocalTime, BookInfoResponse> bookInfoTable,
    LocalDate date
) {

    public static TimeTable createTimeTable(LocalTime startTime, LocalTime endTime,
        LocalDate date) {
        LinkedHashMap<LocalTime, TimeStatus> timeTable = new LinkedHashMap<>();
        LinkedHashMap<LocalTime, BookInfoResponse> bookInfoTable = new LinkedHashMap<>();
        while (startTime.isBefore(endTime)) {
            timeTable.put(startTime, TimeStatus.AVAILABLE);
            bookInfoTable.put(startTime, BookInfoResponse.createEmpty());
            startTime = startTime.plusMinutes(30);
        }
        return TimeTable.builder()
            .date(date)
            .timeTable(timeTable)
            .bookInfoTable(bookInfoTable)
            .build();
    }

    public void updateStatus(LocalTime time, TimeStatus status) {
        if (timeTable.containsKey(time)) {
            timeTable.put(time, status);
        }
    }

    public void updateBookInfo(LocalTime time, RentalHistory rh) {
        if (bookInfoTable.containsKey(time)) {
            bookInfoTable.put(time, BookInfoResponse.fromClass(rh));
        }
    }

    public void updateBookInfo(LocalTime time, Schedule schedule) {
        if (bookInfoTable.containsKey(time)) {
            if (schedule.getScheduleType() == ScheduleType.CLASS) {
                bookInfoTable.put(time, BookInfoResponse.fromClass(schedule));
            } else {
                bookInfoTable.put(time, BookInfoResponse.from(schedule));
            }
        }
    }
}
