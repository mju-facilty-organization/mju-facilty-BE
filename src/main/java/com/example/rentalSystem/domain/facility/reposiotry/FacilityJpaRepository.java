package com.example.rentalSystem.domain.facility.reposiotry;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.type.FacilityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FacilityJpaRepository extends JpaRepository<Facility, Long> {

    Page<Facility> findByFacilityType(FacilityType instanceByValue, Pageable pageable);


    Optional<Facility> findByFacilityNumber(String facilityNumber);

    @Query(value = "SELECT * FROM facility f WHERE f.allowed_boundary LIKE CONCAT('%\"', :affiliationTypeName, '\"%')", nativeQuery = true)
    List<Facility> getFacilityListByAffiliationType(@Param("affiliationTypeName") String affiliationTypeName);

}
