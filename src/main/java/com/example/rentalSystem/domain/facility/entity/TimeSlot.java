package com.example.rentalSystem.domain.facility.entity;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import lombok.Builder;

@Builder
public record TimeSlot(
    LinkedHashMap<LocalTime, RentalStatus> timeSlot
) {

    public static TimeSlot createTimeSlot(LocalTime startTime, LocalTime endTime) {
        LinkedHashMap<LocalTime, RentalStatus> timeSlot = new LinkedHashMap<>();
        while (startTime.isBefore(endTime)) {
            timeSlot.put(startTime, RentalStatus.AVAILABLE);
            startTime = startTime.plusMinutes(30);
        }
        return TimeSlot.builder().timeSlot(timeSlot).build();
    }
}
