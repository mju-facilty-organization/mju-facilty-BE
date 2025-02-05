package com.example.rentalSystem.domain.facility.reposiotry;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.FacilityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityJpaRepository extends JpaRepository<Facility, Long> {

    Page<Facility> findByFacilityType(FacilityType instanceByValue, Pageable pageable);
}
