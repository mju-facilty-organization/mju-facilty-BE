package com.example.rentalSystem.domain.rental.rentalhistory.implement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RentalHistoryUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(
        "yyyy년 M월 d일");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static String formatRentalDate(LocalDateTime rentalStartDate) {
        return rentalStartDate.toLocalDate().format(DATE_FORMATTER);
    }

    public static String formatRentalTime(LocalDateTime rentalStartDate,
        LocalDateTime rentalEndDate) {
        String startTime = rentalStartDate.format(TIME_FORMATTER);
        String endTime = rentalEndDate.format(TIME_FORMATTER);
        return startTime + " ~ " + endTime;
    }
}
