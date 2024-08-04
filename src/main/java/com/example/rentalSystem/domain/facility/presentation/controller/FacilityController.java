package com.example.rentalSystem.domain.facility.presentation.controller;

import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.presentation.api.FacilityApi;
import com.example.rentalSystem.domain.facility.service.FacilityService;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.SuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FacilityController implements FacilityApi {

  private final FacilityService facilityService;

  @Override
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<?> createFacility(
      CreateFacilityRequestDto requestDto
  ) {
    facilityService.create(requestDto);
    return ApiResponse.success(SuccessType.CREATED);
  }
}
