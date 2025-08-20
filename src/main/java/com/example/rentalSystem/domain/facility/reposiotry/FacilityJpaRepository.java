package com.example.rentalSystem.domain.facility.reposiotry;

import com.example.rentalSystem.domain.facility.entity.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * FacilityJpaRepository
 * <p>
 * 정렬 규칙(숫자 자연 정렬): - facility_number의 앞 'S'를 제거한 뒤 숫자 길이(자리수) ASC - 길이가 같으면 숫자 문자열(=자리별) ASC
 * <p>
 * 타입 필터: - :type 이 NULL이면 전체, 아니면 해당 타입만 필터링 (DB 컬럼이 문자열이어야 함)
 * <p>
 * 주의: - 네이티브 SQL 문자열 내부에 '-- ...' 라인 주석/물음표(?) 금지
 */
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