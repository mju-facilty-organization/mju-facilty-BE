package com.example.rentalSystem.domain.book.schedule.entity;

import com.example.rentalSystem.domain.common.BaseTimeEntity;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.type.ScheduleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "schedule_name")
    private String scheduleName;

    @Column(name = "professor_name")
    private String professorName;

    @Column(name = "course_capacity")
    private Integer courseCapacity;

}