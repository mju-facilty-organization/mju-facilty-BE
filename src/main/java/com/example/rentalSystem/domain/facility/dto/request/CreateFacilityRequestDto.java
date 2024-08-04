package com.example.rentalSystem.domain.facility.dto.request;

import com.example.rentalSystem.domain.facility.entity.Facility;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public record CreateFacilityRequestDto(
    String name,
    String location,
    String responsibility,
    Long capacity,
    boolean isAvailable
) {

  public Facility toFacility() {
    return Facility.builder()
        .name(name)
        .location(location)
        .responsibility(responsibility)
        .isAvailable(isAvailable)
        .capacity(capacity)
        .build();
  }
}
