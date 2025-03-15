package com.example.rentalSystem.domain.facility.entity.timeTable;


import com.example.rentalSystem.domain.facility.convert.TimeSlotConverter;
import com.example.rentalSystem.domain.facility.entity.Facility;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(indexes = {
    @Index(name = "idx_facility_date", columnList = "facility_id, date")
})
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
    @Column(columnDefinition = "TEXT")
    private LinkedHashMap<LocalTime, TimeStatus> timeSlot;

    @Builder
    public TimeTable(
        Facility facility,
        LocalDate date,
        LinkedHashMap<LocalTime, TimeStatus> timeSlot
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

    private static LinkedHashMap<LocalTime, TimeStatus> createTimeSlot(LocalTime startTime,
        LocalTime endTime) {
        LinkedHashMap<LocalTime, TimeStatus> timeSlot = new LinkedHashMap<>();
        while (startTime.isBefore(endTime)) {
            timeSlot.put(startTime, TimeStatus.AVAILABLE);
            startTime = startTime.plusMinutes(30);
        }
        return timeSlot;
    }
}
