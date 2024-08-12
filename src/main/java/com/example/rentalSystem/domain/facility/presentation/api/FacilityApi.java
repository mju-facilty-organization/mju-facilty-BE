package com.example.rentalSystem.domain.facility.presentation.api;

import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Facility", description = "Facility API")
public interface FacilityApi {

  @Operation(summary = "[Admin 전용] 시설 생성하기")
  @PostMapping
  ApiResponse<?> createFacility(@RequestBody CreateFacilityRequestDto createFacilityRequestDto);

  @Operation(summary = "[Admin 전용] 시설 수정하기")
  @PutMapping("/{facilityId}")
  ApiResponse<?> updateFacility(@RequestBody UpdateFacilityRequestDto updateFacilityRequestDto, @PathVariable Long facilityId);
}
