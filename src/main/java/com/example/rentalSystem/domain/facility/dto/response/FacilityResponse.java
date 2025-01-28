package com.example.rentalSystem.domain.facility.dto.response;

import com.example.rentalSystem.domain.facility.entity.Facility;
import java.util.List;
import lombok.Builder;

@Builder
public record FacilityResponse(
    //이미지
    String name,
    String location,

    List<String> images,
    Long capacity,
    String allowedBoundary,
    List<String> supportFacilities,
    String pic
) {

    public static FacilityResponse fromFacility(Facility facility) {
        return FacilityResponse.builder()
            .name(facility.getName())
            .location(facility.getLocation())
            .images(facility.getImages())
            .capacity(facility.getCapacity())
            .allowedBoundary(facility.getAllowedBoundary())
            .supportFacilities(facility.getSupportFacilities())
            .pic(facility.getPic())
            .build();
    }
}
