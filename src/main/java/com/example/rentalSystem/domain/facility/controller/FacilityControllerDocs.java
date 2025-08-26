package com.example.rentalSystem.domain.facility.controller;

import com.example.rentalSystem.domain.book.timetable.TimeTable;
import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.request.UpdateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.response.FacilityDetailResponse;
import com.example.rentalSystem.domain.facility.dto.response.FacilityImportResponseDto;
import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.facility.dto.response.PreSignUrlListResponse;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.example.ApiErrorCodeExample;
import com.example.rentalSystem.global.response.type.ErrorType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "시설 관련 API", description = "시설 등록, 조회등에 대한 API")
public interface FacilityControllerDocs {

  @Operation(summary = "시설 등록 API")
  ApiResponse<PreSignUrlListResponse> createFacility(CreateFacilityRequestDto requestDto, boolean overwrite);

  @ApiErrorCodeExample(ErrorType.ENTITY_NOT_FOUND)
  @Operation(summary = "시설 수정 API")
  ApiResponse<?> updateFacility(UpdateFacilityRequestDto requestDto, Long facilityId);

  @Operation(summary = "시설 삭제 API")
  @ApiErrorCodeExample(ErrorType.ENTITY_NOT_FOUND)
  ApiResponse<?> deleteFacility(Long facilityId);

  @Operation(summary = "시설 전체 삭제 API")
  ApiResponse<?> deleteAllFacilities();

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

  @Operation(summary = "시설 기간 타임 테이블 조회 API")
  ApiResponse<List<TimeTable>> getFacilitySchedules(
      @PathVariable("facilityId") Long facilityId,
      @Parameter(description = "조회할 시작 날짜", example = "YYYY-MM-DD")
      LocalDate startDate,
      @Parameter(description = "조회할 끝 날짜", example = "YYYY-MM-DD")
      LocalDate endDate
  );

  @Operation(summary = "시설 XLS 일괄 등록 API")
  ApiResponse<FacilityImportResponseDto> importFacilities(
      @Parameter(description = "XLS/XLSX 파일") @RequestParam("file") MultipartFile file,
      @Parameter(description = "검증만 수행 (DB 반영 안 함)", example = "false") @RequestParam(value = "dryRun", defaultValue = "false") boolean dryRun,
      @Parameter(description = "기존 시설 덮어쓰기", example = "true") @RequestParam(value = "overwrite", defaultValue = "true") boolean overwrite,
      @RequestParam(value = "duplicateAsError", defaultValue = "false") boolean duplicateAsError
  );
}
