package com.example.rentalSystem.domain.facility.implement;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.reposiotry.FacilityJpaRepository;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FacilityImpl {

    private final FacilityJpaRepository facilityJpaRepository;

    /**
     * Retrieves a facility by its unique identifier.
     *
     * @param id the unique identifier of the facility
     * @return the facility entity if found
     * @throws CustomException if no facility with the specified id is found
     */
    public Facility findById(Long id) {
        return facilityJpaRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }

//  public Facility findByNameAndLocation(String facilityName, String location) {
//    return facilityJpaRepository.findByNameAndLocation(facilityName, location)
//        .orElseThrow(() -> new EntityNotFoundException("facility not found :" + facilityName + "," + location));
//  }
}
