package com.example.rentalSystem.domain.facility.controller.dto.request;

import com.example.rentalSystem.domain.affiliation.type.AffiliationType;
import com.example.rentalSystem.domain.facility.entity.Facility;
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
    @Schema(description = "시설 번호", example = "1350")
    String facilityNumber,
    @Schema(description = "파일 이름 리스트", example = "{ \"file1\": \"testFile\" ")
    List<String> fileNames,
    @NotNull
    @Schema(description = "최대 수용 인원", example = "30")
    Long capacity,
    List<String> supportFacilities,
    @Schema(description = "시작 시간", example = "YY:MM")
    LocalTime startTime,
    @Schema(description = "끝 시간", example = "YY:MM")
    LocalTime endTime,
    @NotEmpty
    @Schema(description = "학부", example = "융합소프트웨어학부")
    String college,
    boolean isAvailable
) {

    public Facility toFacility(List<String> imageUrlList, List<AffiliationType> affiliationTypes) {
        return Facility.builder()
            .facilityType(facilityType)
            .facilityNumber(facilityNumber)
            .images(imageUrlList)
            .capacity(capacity)
            .supportFacilities(supportFacilities)
            .startTime(startTime)
            .endTime(endTime)
            .isAvailable(isAvailable)
            .allowedBoundary(affiliationTypes)
            .build();
    }
}

