package com.example.rentalSystem.domain.facility.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;

public record CreateFacilityRequestDto(
    @NotEmpty
    @Schema(description = "시설 타입", example = "본관")
    String facilityType,
    @NotEmpty
    @Schema(description = "시설 번호", example = "S1350")
    String facilityNumber,
    @Schema(description = "파일 이름 리스트", example = "[\"1번사진\",\"2번사진\",\"3번사진\"]")
    List<String> fileNames,
    @NotNull
    @Schema(description = "최대 수용 인원", example = "30")
    Long capacity,
    List<String> supportFacilities,
    @Schema(description = "시작 시간", example = "09:00")
    LocalTime startTime,
    @Schema(description = "끝 시간", example = "21:00")
    LocalTime endTime,
    @NotEmpty
    @Schema(description = "학부", example = "ICT융합대학")
    String college,

    @NotEmpty
    @Schema(description = "이용 범위(전공 리스트)",
        example = "[\"데이터테크놀로지전공\",\"\n"
            + "응용소프트웨어전공\"]")
    List<String> allowedBoundary,


    boolean isAvailable
) {

}