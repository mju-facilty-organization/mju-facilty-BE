package com.example.rentalSystem.domain.facility.dto.response;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record FacilityResponse(
    //이미지
    Long id,
    String facilityType,
    String facilityNumber,

    List<String> images,
    Long capacity,
    String allowedBoundary,
    List<String> supportFacilities,
    String pic
) {

    public static FacilityResponse fromFacility(Facility facility) {
        return FacilityResponse.builder()
            .id(facility.getId())
            .facilityType(facility.getFacilityTypeValue())
            .facilityNumber(facility.getFacilityNumber())
            .images(facility.getImages())
            .capacity(facility.getCapacity())
            .supportFacilities(facility.getSupportFacilities())
            .pic(facility.getPic())
            .build();
    }

    public static FacilityResponse fromRentalHistory(RentalHistory rentalHistory) {
        Facility facility = rentalHistory.getFacility();
        return FacilityResponse.builder()
            .facilityType(facility.getFacilityTypeValue())
            .facilityType(facility.getFacilityNumber())
            .build();
    }
}
