package com.example.rentalSystem.domain.facility.service;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.reposiotry.FacilityJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FacilityReader {

  private final FacilityJpaRepository facilityJpaRepository;

  public List<Facility> getAll() {
    return facilityJpaRepository.findAll();
  }
}
