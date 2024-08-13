package com.example.rentalSystem.domain.facility.reposiotry;

import com.example.rentalSystem.common.fixture.FacilityFixture;
import com.example.rentalSystem.common.support.DataJpaTestSupport;
import com.example.rentalSystem.domain.facility.entity.Facility;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FacilityJpaRepositoryTest extends DataJpaTestSupport {

  @Autowired
  private FacilityJpaRepository facilityJpaRepository;

  @Autowired
  private EntityManager entityManager;

  @DisplayName("시설의 이름과 위치를 사용해 원하는 시설을 찾는다.")
  @Test
  void findByNameAndLocation() {
    // given
    Facility facility = FacilityFixture.createFacility();
    facilityJpaRepository.save(facility);
    // when
    Facility findFacility = facilityJpaRepository.findByNameAndLocation("강의실", "s1353")
        .orElse(null);
    // then
    Assertions.assertThat(findFacility).isNotNull();
    Assertions.assertThat(findFacility.getName()).isEqualTo("강의실");
    Assertions.assertThat(findFacility.getLocation()).isEqualTo("s1353");
  }

  @DisplayName("soft delete가 되는 것을 확인한다.")
  @Test
  void soft_delete() {
    // given
    Facility facility = FacilityFixture.createFacility();
    facilityJpaRepository.save(facility);
    // when
    facilityJpaRepository.delete(facility);
    entityManager.flush();
    entityManager.clear();
    // then
    Facility deletedFacility = (Facility) entityManager.createNativeQuery(
            "SELECT * FROM facility WHERE name = :name", Facility.class)
        .setParameter("name", facility.getName())
        .getSingleResult();

    Assertions.assertThat(facilityJpaRepository.findAll()).isEmpty();
    Assertions.assertThat(deletedFacility.isDeleted()).isTrue();
  }
}