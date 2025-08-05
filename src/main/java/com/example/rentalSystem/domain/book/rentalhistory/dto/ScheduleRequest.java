package com.example.rentalSystem.domain.book.rentalhistory.dto;

import com.example.rentalSystem.domain.book.schedule.entity.Schedule;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.type.ScheduleType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleRequest(
    @Schema(description = "요일", example = "MONDAY")
    @NotNull(message = "요일은 필수입니다.")
    DayOfWeek dayOfWeek,

    @Schema(type = "string", description = "대여 시작 시간", example = "10:00")
    @NotNull(message = "시작 시간은 필수입니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    LocalTime rentalStartTime,

    @Schema(type = "string", description = "대여 시작 시간", example = "12:00")
    @NotNull(message = "종료 시간은 필수입니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    LocalTime rentalEndTime
) {


    public Schedule toEntity(Facility facility, String organization, ScheduleType scheduleType,
        LocalDate validStartDate, LocalDate validEndDate) {
        return Schedule
            .builder()
            .facility(facility)
            .organization(organization)
            .scheduleType(scheduleType)
            .validStartDate(validStartDate)
            .validEndDate(validEndDate)
            .rentalStartTime(rentalStartTime)
            .rentalEndTime(rentalEndTime)
            .dayOfWeek(dayOfWeek)
            .build();
    }
}
