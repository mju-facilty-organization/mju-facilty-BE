package com.example.rentalSystem.domain.facility.controller;

import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.request.UpdateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.response.FacilityDetailResponse;
import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.facility.dto.response.PreSignUrlListResponse;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.example.ApiErrorCodeExample;
import com.example.rentalSystem.global.response.type.ErrorType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Tag(name = "시설 관련 API", description = "시설 등록, 조회등에 대한 API")
public interface FacilityControllerDocs {

    @Operation(summary = "시설 등록 API")
    ApiResponse<PreSignUrlListResponse> createFacility(CreateFacilityRequestDto requestDto);

    @ApiErrorCodeExample(ErrorType.ENTITY_NOT_FOUND)
    @Operation(summary = "시설 수정 API")
    ApiResponse<?> updateFacility(UpdateFacilityRequestDto requestDto, Long facilityId);

    @Operation(summary = "시설 삭제 API")
    @ApiErrorCodeExample(ErrorType.ENTITY_NOT_FOUND)
    ApiResponse<?> deleteFacility(Long facilityId);

    @Operation(summary = "시설 전체 조회 API")
    @ApiErrorCodeExample(ErrorType.INVALID_FACILITY_TYPE)
    ApiResponse<Page<FacilityResponse>> getAllFacility(
        @Parameter(example = "{\n"
            + "  \"page\": 0,\n"
            + "  \"size\": 10\n"
            + "}")
        Pageable pageable,
        @Parameter(example = "본관")
        String facilityType
    );

    @Operation(summary = "시설 상세 조회 API")
    @ApiErrorCodeExample(ErrorType.ENTITY_NOT_FOUND)
    ApiResponse<FacilityDetailResponse> getFacilityDetail(
        Long facilityId,
        @Parameter(description = "조회할 날짜", example = "YYYY-MM-DD")
        LocalDate date);
}
