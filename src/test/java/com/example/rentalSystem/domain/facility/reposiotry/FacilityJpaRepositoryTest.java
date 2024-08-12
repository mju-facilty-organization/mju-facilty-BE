package com.example.rentalSystem.domain.facility.reposiotry;

import com.example.rentalSystem.common.fixture.FacilityFixture;
import com.example.rentalSystem.common.support.DataJpaTestSupport;
import com.example.rentalSystem.domain.facility.entity.Facility;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FacilityJpaRepositoryTest extends DataJpaTestSupport {

  @Autowired
  private FacilityJpaRepository facilityJpaRepository;

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
}