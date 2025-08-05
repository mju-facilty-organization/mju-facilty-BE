package com.example.rentalSystem.domain.book.schedule.dto.request;

import com.example.rentalSystem.domain.book.rentalhistory.dto.ScheduleRequest;
import com.example.rentalSystem.domain.book.schedule.entity.Schedule;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.type.ScheduleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record CreateRegularScheduleRequest(
    @Schema(description = "단체명 또는 조직", example = "COW")
    @NotNull(message = "단체명은 필수입니다.")
    String organization,

    @Schema(description = "스케줄 타입", example = "CLASS")
    @NotNull(message = "스케줄 타입은 필수입니다.")
    ScheduleType scheduleType,

    @Schema(description = "시설 ID", example = "1")
    @NotNull(message = "시설 ID는 필수입니다.")
    Long facilityId,

    @Schema(type = "array", description = "정기 스케줄 목록",
        example = "[{\"dayOfWeek\": \"MONDAY\", \"rentalStartTime\": \"10:00\", \"rentalEndTime\": \"12:00\"}]"
    )
    @NotNull(message = "스케줄 목록은 필수입니다.")
    List<ScheduleRequest> schedules,

    @Schema(description = "스케줄 유효 시작일", example = "2025-08-01")
    @NotNull(message = "유효 시작일은 필수입니다.")
    LocalDate validStartDate,

    @Schema(type = "string", description = "스케줄 유효 종료일", example = "2025-12-31")
    @NotNull(message = "유효 종료일은 필수입니다.")
    LocalDate validEndDate
) {

    public List<Schedule> toEntities(Facility facility) {
        return this.schedules.stream()
            .map(scheduleDto -> scheduleDto.toEntity(
                    facility,
                    organization,
                    scheduleType,
                    validStartDate,
                    validEndDate
                )
            )
            .collect(Collectors.toList());
    }
}
