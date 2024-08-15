package com.example.rentalSystem.domain.facility.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.rentalSystem.common.fixture.FacilityFixture;
import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.request.UpdateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.implement.FacilityFinder;
import com.example.rentalSystem.domain.facility.implement.FacilityReader;
import com.example.rentalSystem.domain.facility.implement.FacilityRemover;
import com.example.rentalSystem.domain.facility.implement.FacilitySaver;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FacilityServiceTest {

  @InjectMocks
  private FacilityService facilityService;

  @Mock
  private FacilityFinder facilityFinder;

  @Mock
  private FacilitySaver facilitySaver;

  @Mock
  private FacilityReader facilityReader;

  @Mock
  private FacilityRemover facilityRemover;

  @DisplayName("시설 생성된 후 저장이 되었는지 테스트 합니다.")
  @Test
  void 시설_생성() {
    // given
    CreateFacilityRequestDto createFacilityRequestDto = FacilityFixture.createFacilityRequestDto();
    Facility expectedFacility = createFacilityRequestDto.toFacility();

    given(facilitySaver.save(any(Facility.class))).willReturn(expectedFacility);
    // when
    facilityService.create(createFacilityRequestDto);
    // then
    verify(facilitySaver, times(1)).save(argThat(facility ->
        facility.getName().equals(expectedFacility.getName())
    ));
  }

  @DisplayName("시설을 수정할 수 있습니다.")
  @Test
  void 시설_수정() {
    // given
    UpdateFacilityRequestDto updateFacilityRequestDto = FacilityFixture.createUpdateFacilityRequestDto();
    Facility originFacility = FacilityFixture.createFacility();
    Long originFacilityId = 0L;
    given(facilityFinder.findById(originFacilityId)).willReturn(originFacility);
    // when
    facilityService.update(updateFacilityRequestDto, originFacilityId);
    // then
    verify(facilityFinder, times(1)).findById(originFacilityId);
    assertEquals("수정된 이름", originFacility.getName());
    assertEquals("수정된 위치", originFacility.getLocation());
  }

  @DisplayName("시설을 전체 조회할 수 있습니다.")
  @Test
  void 시설_전체_조회() {
    // given
    Facility facility = FacilityFixture.createFacility();
    Facility facility2 = FacilityFixture.createUpdateFacility();
    Facility facility3 = FacilityFixture.createFacility();

    given(facilityReader.getAll()).willReturn(new ArrayList<>(List.of(facility, facility2, facility3)));
    // when
    List<FacilityResponse> facilities = facilityService.getAll();
    // then
    Assertions.assertThat(facilities).hasSize(3);
    Assertions.assertThat(facilities).extracting("name").containsExactly("강의실", "수정된 강의실", "강의실");
  }

  @DisplayName("존재하는 시설을 성공적으로 삭제한다.")
  @Test
  void 시설_삭제() {
    // given
    Long facilityId = 1L;
    Facility facility = FacilityFixture.createFacility();
    given(facilityFinder.findById(facilityId)).willReturn(facility);
    // when
    facilityService.delete(facilityId);
    // then
    verify(facilityFinder).findById(facilityId);
    verify(facilityRemover).delete(facility);
  }
}
