package com.example.rentalSystem.domain.facility.controller;

import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.request.UpdateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.facility.dto.response.PresignUrlListResponse;
import com.example.rentalSystem.domain.facility.service.FacilityService;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.SuccessType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin/facilities", produces = "application/json;charset=utf-8")
public class FacilityController {

    private final FacilityService facilityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> createFacility(@RequestBody CreateFacilityRequestDto requestDto) {
        PresignUrlListResponse presignUrlListResponse = facilityService.create(requestDto);
        return ApiResponse.success(SuccessType.CREATED, presignUrlListResponse);
    }

    @PutMapping("/{facilityId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> updateFacility(UpdateFacilityRequestDto requestDto, Long facilityId) {
        facilityService.update(requestDto, facilityId);
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    @DeleteMapping("/{facilityId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> deleteFacility(Long facilityId) {
        facilityService.delete(facilityId);
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<FacilityResponse>> getAllFacility() {
        List<FacilityResponse> facilityResponses = facilityService.getAll();
        return ApiResponse.success(SuccessType.SUCCESS, facilityResponses);
    }
}
