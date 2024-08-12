package com.example.rentalSystem.domain.facility.service;

import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.reposiotry.FacilityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacilityService {

  private final FacilityJpaRepository facilityJpaRepository;

  @Transactional
  public void create(CreateFacilityRequestDto createFacilityRequestDto) {
    Facility facility = createFacilityRequestDto.toFacility();
    facilityJpaRepository.save(facility);
  }

  @Transactional
  public void update(UpdateFacilityRequestDto requestDto, Long facilityId) {
    Facility originFacility = facilityFinder.findById(facilityId);
    Facility updateFacility = requestDto.toFacility();
    originFacility.update(updateFacility);
  }

  public void delete(Long facilityId) {
    Facility facility = facilityFinder.findById(facilityId);
    facilityRemover.delete(facility);
  }

  public List<FacilityResponse> getAllFacility() {
    List<Facility> facilities = facilityReader.getAll();

    return facilities.stream()
        .map(FacilityResponse::fromFacility)
        .toList();
  }
}
