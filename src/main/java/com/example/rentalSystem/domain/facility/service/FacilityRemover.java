package com.example.rentalSystem.domain.facility.service;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.reposiotry.FacilityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FacilityRemover {

  private final FacilityJpaRepository facilityJpaRepository;

  public void delete(Facility facility) {
    facilityJpaRepository.delete(facility);
  }
}
