package com.example.rentalSystem.domain.facility.reposiotry;

import com.example.rentalSystem.domain.facility.entity.Facility;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FacilityJpaRepository extends JpaRepository<Facility, Long> {

    @Query(
        value = """
            SELECT *
            FROM facility f
            WHERE (:type IS NULL OR f.facility_type = :type)
            ORDER BY
              CHAR_LENGTH(SUBSTRING(f.facility_number, 2)) ASC,
              SUBSTRING(f.facility_number, 2) ASC
            """,
        countQuery = """
            SELECT COUNT(*)
            FROM facility f
            WHERE (:type IS NULL OR f.facility_type = :type)
            """,
        nativeQuery = true
    )
    Page<Facility> findByFacilityType(@Param("type") String type, Pageable pageable);

    Optional<Facility> findByFacilityNumber(String facilityNumber);

    @Query(
        value = "SELECT * FROM facility f " +
            "WHERE f.allowed_boundary LIKE CONCAT('%\"', :affiliationTypeName, '\"%')",
        nativeQuery = true
    )
    List<Facility> getFacilityListByAffiliationType(@Param("affiliationTypeName") String affiliationTypeName);
}