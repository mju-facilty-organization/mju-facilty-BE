package com.example.rentalSystem.domain.facility.implement;

import com.example.rentalSystem.domain.facility.dto.FacilityDto;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.reposiotry.FacilityJpaRepository;
import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
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

    public List<FacilityDto> getFacilityListBy(AffiliationType affiliationType) {
        List<Facility> facilityListByDepartment = facilityJpaRepository.getFacilityListByAffiliationType(
            affiliationType.name());
        return facilityListByDepartment.stream()
            .map(FacilityDto::from)
            .toList();
    }
}
