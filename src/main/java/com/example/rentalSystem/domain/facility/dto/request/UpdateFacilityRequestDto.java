package com.example.rentalSystem.domain.facility.dto.request;

import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;

import java.util.List;

public record UpdateFacilityRequestDto(
        String facilityType,
        String facilityNumber,
        List<String> supportFacilities,
        java.time.LocalTime startTime,
        java.time.LocalTime endTime,
        Long capacity,
        Boolean isAvailable,
        List<AffiliationType> allowedBoundary,
        List<String> addFileNames,
        List<String> removeKeys,
        List<String> newOrder,
        Boolean hardDelete

) {

}