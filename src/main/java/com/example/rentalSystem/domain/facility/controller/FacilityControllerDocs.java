package com.example.rentalSystem.domain.facility.controller;

import com.example.rentalSystem.domain.facility.controller.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.controller.dto.request.UpdateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.controller.dto.response.FacilityDetailResponse;
import com.example.rentalSystem.domain.facility.controller.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.facility.controller.dto.response.PreSignUrlListResponse;
import com.example.rentalSystem.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Tag(name = "시설 관련 API", description = "시설 등록, 조회등에 대한 API")
public interface FacilityControllerDocs {

    @Operation(summary = "시설 등록 API")
    ApiResponse<PreSignUrlListResponse> createFacility(CreateFacilityRequestDto requestDto);

    @Operation(summary = "시설 수정 API")
    ApiResponse<?> updateFacility(UpdateFacilityRequestDto requestDto, Long facilityId);

    @Operation(summary = "시설 삭제 API")
    ApiResponse<?> deleteFacility(Long facilityId);

    @Operation(summary = "시설 전체 조회 API")
    ApiResponse<Page<FacilityResponse>> getAllFacility(
        Pageable pageable, String facilityType
    );

    @Operation(summary = "시설 전체 상세 조회 API")
    ApiResponse<FacilityDetailResponse> getFacilityDetail(
        Long facilityId, LocalDate date);
}
