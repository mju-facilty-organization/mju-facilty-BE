package com.example.rentalSystem.domain.chatbot.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record ExtractResponse(
    String intent,
    String department,
    String date,
    String time,
    String facilityType
) {

    public ExtractResponse fillDateAndTime() {

        String newDate = (date == null || date.isBlank()) ?
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : date;

        String newTime = (time == null || time.isBlank()) ?
            LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) : time;

        return new ExtractResponse(intent, department, newDate, newTime, facilityType);
    }
}
