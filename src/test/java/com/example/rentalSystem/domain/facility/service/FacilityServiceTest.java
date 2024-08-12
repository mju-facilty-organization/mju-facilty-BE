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
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.implement.FacilityFinder;
import com.example.rentalSystem.domain.facility.implement.FacilitySaver;
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
}