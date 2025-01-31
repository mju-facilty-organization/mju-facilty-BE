package com.example.rentalSystem.domain.facility.dto.request;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.FacilityType;
import java.time.LocalTime;
import java.util.List;

public record UpdateFacilityRequestDto(
    FacilityType facilityType,
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
            .facilityType(this.facilityType)
            .capacity(this.capacity)
            .facilityNumber(facilityNumber)
            .supportFacilities(supportFacilities)
            .isAvailable(isAvailable)
            .build();
    }
}
