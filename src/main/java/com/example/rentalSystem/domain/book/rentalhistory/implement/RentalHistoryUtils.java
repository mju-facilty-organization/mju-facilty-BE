package com.example.rentalSystem.domain.book.rentalhistory.implement;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class RentalHistoryUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(
        "yyyy년 M월 d일");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static String formatRentalDate(LocalDate rentalStartDate) {
        return rentalStartDate.format(DATE_FORMATTER);
    }

    public static String formatRentalTime(LocalTime rentalStartTime,
        LocalTime rentalEndTime) {
        String startTime = rentalStartTime.format(TIME_FORMATTER);
        String endTime = rentalEndTime.format(TIME_FORMATTER);
        return startTime + " ~ " + endTime;
    }
}
