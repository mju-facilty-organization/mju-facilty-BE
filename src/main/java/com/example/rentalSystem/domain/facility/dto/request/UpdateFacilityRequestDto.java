package com.example.rentalSystem.domain.facility.dto.request;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.FacilityType;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ErrorType;
import java.time.LocalTime;
import java.util.List;

public record UpdateFacilityRequestDto(
    String facilityType,
    String facilityNumber,
    String location,
    String chargeProfessor,
    List<String> supportFacilities,
    List<String> possibleDays,
    LocalTime startTime,
    LocalTime endTime,
    long capacity,
    boolean isAvailable
) {

    public Facility toFacility() {
        if (FacilityType.existsByValue(facilityType)) {
            return Facility.builder()
                .facilityType((facilityType))
                .capacity(capacity)
                .facilityNumber(facilityNumber)
                .supportFacilities(supportFacilities)
                .isAvailable(isAvailable)
                .build();
        }
        throw new CustomException(ErrorType.INVALID_REQUEST);
    }
}
