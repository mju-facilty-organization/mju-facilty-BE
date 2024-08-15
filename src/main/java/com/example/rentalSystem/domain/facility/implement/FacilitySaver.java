package com.example.rentalSystem.domain.facility.implement;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.reposiotry.FacilityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FacilitySaver {

  private final FacilityJpaRepository facilityJpaRepository;

  public Facility save(Facility facility) {
    return facilityJpaRepository.save(facility);
  }
}
