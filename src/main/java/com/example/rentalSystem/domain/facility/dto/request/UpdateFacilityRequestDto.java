package com.example.rentalSystem.domain.facility.dto.request;

import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;

public record UpdateFacilityRequestDto(
    String facilityType,
    String facilityNumber,
    List<String> supportFacilities,
    @NotNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    LocalTime startTime,
    @NotNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    LocalTime endTime,
    Long capacity,
    Boolean isAvailable,
    List<AffiliationType> allowedBoundary,
    List<String> addFileNames,
    List<String> removeKeys,
    List<String> newOrder,
    Boolean hardDelete

) {

}