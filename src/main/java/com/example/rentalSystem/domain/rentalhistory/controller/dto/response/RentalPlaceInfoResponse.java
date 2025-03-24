package com.example.rentalSystem.domain.rentalhistory.controller.dto.response;

import com.example.rentalSystem.domain.facility.entity.FacilityType;

public record RentalPlaceInfoResponse(
    String facilityNumber,
    FacilityType facilityType,
    String date,
    String time,
    int count
) {

    public static RentalPlaceInfoResponse of(
        RentalHistoryResponseDto rentalHistoryResponseDto,
        String date,
        String time
    ) {
        return new RentalPlaceInfoResponse(
            rentalHistoryResponseDto.getFacilityResponse().facilityNumber(),
            rentalHistoryResponseDto.getFacilityResponse().facilityType(),
            date,
            time,
            10
        );
    }
}
