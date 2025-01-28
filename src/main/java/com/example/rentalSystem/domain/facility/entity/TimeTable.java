package com.example.rentalSystem.domain.facility.entity;


import com.example.rentalSystem.domain.facility.convert.TimeSlotConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class TimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Facility facility;

    @Column
    private LocalDate today;

    @Convert(converter = TimeSlotConverter.class)
    private TimeSlot timeSlot;

    @Builder
    public TimeTable(
        Facility facility,
        LocalDate today,
        TimeSlot timeSlot
    ) {
        this.facility = facility;
        this.today = today;
        this.timeSlot = timeSlot;
    }

    public static TimeTable toEntity(
        Facility facility,
        LocalDate today, LocalTime startTime,
        LocalTime endTime) {
        TimeSlot timeSlot = TimeSlot.createTimeSlot(startTime, endTime);

        return TimeTable.builder()
            .facility(facility)
            .today(today)
            .timeSlot(timeSlot)
            .build();
    }
}
