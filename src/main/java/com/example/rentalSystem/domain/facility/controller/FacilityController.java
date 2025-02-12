package com.example.rentalSystem.domain.facility.controller;

import com.example.rentalSystem.domain.common.PagedResponse;
import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.request.UpdateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.response.FacilityDetailResponse;
import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.facility.dto.response.PreSignUrlListResponse;
import com.example.rentalSystem.domain.facility.service.FacilityService;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.SuccessType;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin/facilities", produces = "application/json;charset=utf-8")
public class FacilityController {

    private final FacilityService facilityService;

    @PostMapping
    public ApiResponse<PreSignUrlListResponse> createFacility(
        @RequestBody CreateFacilityRequestDto requestDto) {
        PreSignUrlListResponse presignUrlListResponse = facilityService.create(requestDto);
        return ApiResponse.success(SuccessType.CREATED, presignUrlListResponse);
    }

    @PutMapping("/{facilityId}")
    public ApiResponse<?> updateFacility(UpdateFacilityRequestDto requestDto, Long facilityId) {
        facilityService.update(requestDto, facilityId);
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    @DeleteMapping("/{facilityId}")
    public ApiResponse<?> deleteFacility(Long facilityId) {
        facilityService.delete(facilityId);
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    @GetMapping
    public ApiResponse<PagedResponse<FacilityResponse>> getAllFacility(
        @PageableDefault(size = 10) Pageable pageable,
        @RequestParam(value = "facility-type", required = false) String facilityType
    ) {
        PagedResponse<FacilityResponse> facilityResponses = facilityService.getAll(pageable,
            facilityType);
        return ApiResponse.success(SuccessType.SUCCESS, facilityResponses);
    }

    @GetMapping("/{facilityId}")
    public ApiResponse<FacilityDetailResponse> getFacilityDetail(
        @PathVariable("facilityId") Long facilityId,
        @RequestParam(name = "date") LocalDate date
    ) {
        FacilityDetailResponse facilityDetailResponse = facilityService.getFacilityDetail(
            facilityId, date);
        return ApiResponse.success(SuccessType.SUCCESS, facilityDetailResponse);
    }
}
