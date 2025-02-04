package com.example.rentalSystem.domain.facility.dto.request;

import com.example.rentalSystem.domain.facility.entity.facility.Facility;
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
        return Facility.builder()
            .facilityType((facilityType))
            .capacity(capacity)
            .facilityNumber(facilityNumber)
            .supportFacilities(supportFacilities)
            .isAvailable(isAvailable)
            .build();
    }
}
