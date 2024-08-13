package com.example.rentalSystem.domain.facility.presentation.controller;

import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.request.UpdateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.facility.presentation.api.FacilityApi;
import com.example.rentalSystem.domain.facility.service.FacilityService;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.SuccessType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FacilityController implements FacilityApi {

  private final FacilityService facilityService;

  @Override
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<?> createFacility(CreateFacilityRequestDto requestDto) {
    facilityService.create(requestDto);
    return ApiResponse.success(SuccessType.CREATED);
  }

  @Override
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse<?> updateFacility(UpdateFacilityRequestDto requestDto, Long facilityId) {
    facilityService.update(requestDto, facilityId);
    return ApiResponse.success(SuccessType.SUCCESS);
  }

  @Override
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse<?> deleteFacility(Long facilityId) {
    facilityService.delete(facilityId);
    return ApiResponse.success(SuccessType.SUCCESS);
  }

  @Override
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse<List<FacilityResponse>> getAllFacility() {
    List<FacilityResponse> facilityResponses = facilityService.getAll();
    return ApiResponse.success(SuccessType.SUCCESS, facilityResponses);
  }
}
