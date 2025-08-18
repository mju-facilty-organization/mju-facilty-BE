package com.example.rentalSystem.domain.facility.dto;

import com.example.rentalSystem.domain.facility.entity.Facility;
import lombok.Builder;

@Builder
public record FacilityDto(
    String facilityType,
    String facilityNumber,
    Long capacity
) {

    public static FacilityDto from(Facility facility) {
        return FacilityDto.builder()
            .facilityNumber(facility.getFacilityNumber())
            .facilityType(facility.getFacilityType().getValue())
            .capacity(facility.getCapacity())
            .build();
    }
}
