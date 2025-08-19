package com.example.rentalSystem.domain.book.schedule.controller;

import com.example.rentalSystem.domain.book.schedule.dto.request.CreateRegularScheduleRequest;
import com.example.rentalSystem.domain.book.schedule.service.ScheduleImportService;
import com.example.rentalSystem.domain.book.schedule.service.ScheduleService;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.type.SuccessType;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin/schedules", produces = "application/json;charset=utf-8")
public class ScheduleController implements ScheduleControllerDocs {

  private final ScheduleService scheduleService;

  private final ScheduleImportService scheduleImportService;

  @PostMapping("/regular")
  public ApiResponse<?> createRegularFacility(
      @Valid @RequestBody CreateRegularScheduleRequest request
  ) {
    scheduleService.createSchedule(request);
    return ApiResponse.success(SuccessType.SUCCESS);
  }

  @Override
  @PostMapping(path = "/upload-excel", consumes = "multipart/form-data")
  public ApiResponse<?> uploadExcel(
      @RequestParam("file") MultipartFile file,
      @RequestParam(required = false) LocalDate validStartDate,
      @RequestParam(required = false) LocalDate validEndDate,
      @RequestParam(defaultValue = "true") boolean overwrite
  ) {
    LocalDate start = (validStartDate != null) ? validStartDate : LocalDate.of(2025, 8, 1);
    LocalDate end = (validEndDate != null) ? validEndDate : LocalDate.of(2025, 12, 31);

    var result = scheduleImportService.importExcel(file, start, end, overwrite);
    return ApiResponse.success(SuccessType.SUCCESS, result);
  }
}

