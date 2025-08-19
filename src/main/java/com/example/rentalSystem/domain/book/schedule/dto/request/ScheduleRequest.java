package com.example.rentalSystem.domain.book.schedule.dto.request;

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
    @Schema(description = "요일") @NotNull DayOfWeek dayOfWeek,
    @Schema(description = "대여 시작 시간", example = "10:00")
    @NotNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    LocalTime rentalStartTime,
    @Schema(description = "대여 종료 시간", example = "12:00")
    @NotNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    LocalTime rentalEndTime,
    @Schema(description = "과목명") String scheduleName,
    @Schema(description = "담당 교수") String professorName,
    @Schema(description = "수용 인원") Integer courseCapacity
) {

  public Schedule toEntity(
      Facility facility, String organization, ScheduleType scheduleType,
      LocalDate validStartDate, LocalDate validEndDate
  ) {
    return Schedule.builder()
        .facility(facility)
        .organization(organization)
        .scheduleType(scheduleType)
        .validStartDate(validStartDate)
        .validEndDate(validEndDate)
        .rentalStartTime(rentalStartTime)
        .rentalEndTime(rentalEndTime)
        .dayOfWeek(dayOfWeek)
        .scheduleName(scheduleName)
        .professorName(professorName)
        .courseCapacity(courseCapacity)
        .build();
  }
}