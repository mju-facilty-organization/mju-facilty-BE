package com.example.rentalSystem.domain.facility.dto.response;

import com.example.rentalSystem.domain.facility.entity.Facility;
import java.util.List;
import lombok.Builder;

@Builder
public record FacilityResponse(
    //이미지
    String name,
    String location,
    Long capacity,
    List<String> supportFacilities
) {

  public static FacilityResponse fromFacility(Facility facility) {
    return FacilityResponse.builder()
        .name(facility.getName())
        .location(facility.getLocation())
        .capacity(facility.getCapacity())
        .supportFacilities(facility.getSupportFacilities())
        .build();
  }
}
