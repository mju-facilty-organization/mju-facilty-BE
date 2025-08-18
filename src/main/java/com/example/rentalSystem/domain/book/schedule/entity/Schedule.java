package com.example.rentalSystem.domain.book.schedule.entity;

import com.example.rentalSystem.domain.common.BaseTimeEntity;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.type.ScheduleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Facility facility;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime rentalStartTime;

    @Column(nullable = false)
    private LocalTime rentalEndTime;

    @Column(nullable = false)
    private LocalDate validStartDate;

    @Column(nullable = false)
    private LocalDate validEndDate;

    @Column(nullable = false)
    private String organization;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleType scheduleType;

    @Column(name = "schedule_name", nullable = false)
    private String scheduleName;

    @Column(name = "professor_name", nullable = true)
    private String professorName;

    @Column(name = "course_capacity", nullable = true)
    private Integer courseCapacity;

}