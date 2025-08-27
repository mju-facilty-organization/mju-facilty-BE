package com.example.rentalSystem.domain.facility.controller;

import com.example.rentalSystem.domain.book.timetable.TimeTable;
import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.request.UpdateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.response.FacilityDetailResponse;
import com.example.rentalSystem.domain.facility.dto.response.FacilityImportResponseDto;
import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.facility.dto.response.PreSignUrlListResponse;
import com.example.rentalSystem.domain.facility.service.FacilityImportService;
import com.example.rentalSystem.domain.facility.service.FacilityService;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.type.SuccessType;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin/facilities", produces = "application/json;charset=utf-8")
public class FacilityController implements FacilityControllerDocs {

  private final FacilityService facilityService;

  private final FacilityImportService facilityImportService;


  @Override
  @DeleteMapping("/{facilityId}")
  public ApiResponse<?> deleteFacility(@PathVariable Long facilityId) {
    facilityService.delete(facilityId);
    return ApiResponse.success(SuccessType.SUCCESS);
  }

  // ★ 전체 삭제 API 추가
  @DeleteMapping("/all")
  public ApiResponse<?> deleteAllFacilities() {
    facilityService.deleteAllFacilities();
    return ApiResponse.success(SuccessType.SUCCESS);
  }

  @Override
  @PostMapping
  public ApiResponse<PreSignUrlListResponse> createFacility(
      @Valid @RequestBody CreateFacilityRequestDto requestDto,
      @RequestParam(value = "overwrite", defaultValue = "false") boolean overwrite) {
    PreSignUrlListResponse presignUrlListResponse = facilityService.create(requestDto, overwrite);
    return ApiResponse.success(SuccessType.CREATED, presignUrlListResponse);
  }

  @Override
  @PutMapping("/{facilityId}")
  public ApiResponse<PreSignUrlListResponse> updateFacility(
      @RequestBody UpdateFacilityRequestDto requestDto,
      @PathVariable Long facilityId
  ) {
    PreSignUrlListResponse presigns = facilityService.update(requestDto, facilityId);
    return ApiResponse.success(SuccessType.SUCCESS, presigns);
  }

  @Override
  @GetMapping
  public ApiResponse<Page<FacilityResponse>> getAllFacility(
      @PageableDefault(size = 10) Pageable pageable,
      @RequestParam(value = "facility-type", required = false) String facilityType
  ) {
    Page<FacilityResponse> facilityResponses = facilityService.getAll(pageable, facilityType);
    return ApiResponse.success(SuccessType.SUCCESS, facilityResponses);
  }

  @Override
  @GetMapping("/{facilityId}")
  public ApiResponse<FacilityDetailResponse> getFacilityDetail(
      @PathVariable("facilityId") Long facilityId,
      @RequestParam(name = "date", required = false) LocalDate date
  ) {
    if (date == null) {
      date = LocalDate.now();
    }

    FacilityDetailResponse facilityDetailResponse = facilityService.getFacilityDetail(
        facilityId, date
    );
    return ApiResponse.success(SuccessType.SUCCESS, facilityDetailResponse);
  }

  @Override
  @GetMapping("/{facilityId}/schedules")
  public ApiResponse<List<TimeTable>> getFacilitySchedules(
      @PathVariable("facilityId") Long facilityId,
      @RequestParam(name = "startDate") LocalDate startDate,
      @RequestParam(name = "endDate") LocalDate endDate
  ) {
    List<TimeTable> timeTables = facilityService.getFacilityWeeklySchedule(
        facilityId, startDate, endDate
    );
    return ApiResponse.success(SuccessType.SUCCESS, timeTables);
  }

  @Override
  @PostMapping(path = "/import", consumes = "multipart/form-data")
  public ApiResponse<FacilityImportResponseDto> importFacilities(
      @RequestParam("file") MultipartFile file,
      @RequestParam(value = "dryRun", defaultValue = "false") boolean dryRun,
      @RequestParam(value = "overwrite", defaultValue = "true") boolean overwrite,
      @RequestParam(value = "duplicateAsError", defaultValue = "false") boolean duplicateAsError

  ) {
    // FacilityController.importFacilities(...)
    return ApiResponse.success(
        SuccessType.SUCCESS,
        facilityImportService.importFacilities(file, dryRun, overwrite, duplicateAsError)
    );
  }

}