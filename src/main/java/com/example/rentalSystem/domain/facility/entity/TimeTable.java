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
import java.util.LinkedHashMap;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Facility facility;

    @Column
    private LocalDate date;

    @Convert(converter = TimeSlotConverter.class)
    private LinkedHashMap<LocalTime, RentalStatus> timeSlot;

    @Builder
    public TimeTable(
        Facility facility,
        LocalDate date,
        LinkedHashMap<LocalTime, RentalStatus> timeSlot
    ) {
        this.facility = facility;
        this.date = date;
        this.timeSlot = timeSlot;
    }

    public static TimeTable toEntity(
        Facility facility,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime) {

        return TimeTable.builder()
            .facility(facility)
            .date(date)
            .timeSlot(createTimeSlot(startTime, endTime))
            .build();
    }

    private static LinkedHashMap<LocalTime, RentalStatus> createTimeSlot(LocalTime startTime,
        LocalTime endTime) {
        LinkedHashMap<LocalTime, RentalStatus> timeSlot = new LinkedHashMap<>();
        while (startTime.isBefore(endTime)) {
            timeSlot.put(startTime, RentalStatus.AVAILABLE);
            startTime = startTime.plusMinutes(30);
        }
        return timeSlot;
    }
}
