package com.example.rentalSystem.domain.facility.dto.request;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.FacilityType;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ErrorType;
import java.time.LocalTime;
import java.util.List;

public record CreateFacilityRequestDto(
    String facilityType,
    String facilityNumber,
    List<String> fileNames,
    Long capacity,
    String allowedBoundary,
    List<String> supportFacilities,
    String pic,
    LocalTime startTime,
    LocalTime endTime,
    boolean isAvailable
) {

    public Facility toFacility(List<String> imageUrlList) {
        if (FacilityType.existsByValue(facilityType)) {
            return Facility.builder()
                .facilityType(facilityType)
                .facilityNumber(facilityNumber)
                .images(imageUrlList)
                .capacity(capacity)
                .allowedBoundary(allowedBoundary)
                .supportFacilities(supportFacilities)
                .pic(pic)
                .startTime(startTime)
                .endTime(endTime)
                .isAvailable(isAvailable)
                .build();
        }
        throw new CustomException(ErrorType.INVALID_REQUEST);
    }
}
