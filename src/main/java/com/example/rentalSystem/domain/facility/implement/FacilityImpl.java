package com.example.rentalSystem.domain.facility.implement;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.reposiotry.FacilityJpaRepository;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FacilityImpl {

    private final FacilityJpaRepository facilityJpaRepository;

    public Facility findById(Long id) {
        return facilityJpaRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }

//  public Facility findByNameAndLocation(String facilityName, String location) {
//    return facilityJpaRepository.findByNameAndLocation(facilityName, location)
//        .orElseThrow(() -> new EntityNotFoundException("facility not found :" + facilityName + "," + location));
//  }
}
