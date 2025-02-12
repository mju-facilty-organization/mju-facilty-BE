package com.example.rentalSystem.domain.facility.implement;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.reposiotry.FacilityJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FacilityImpl {

    private final FacilityJpaRepository facilityJpaRepository;

    public Facility findById(Long id) {
        return facilityJpaRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

//  public Facility findByNameAndLocation(String facilityName, String location) {
//    return facilityJpaRepository.findByNameAndLocation(facilityName, location)
//        .orElseThrow(() -> new EntityNotFoundException("facility not found :" + facilityName + "," + location));
//  }
}
