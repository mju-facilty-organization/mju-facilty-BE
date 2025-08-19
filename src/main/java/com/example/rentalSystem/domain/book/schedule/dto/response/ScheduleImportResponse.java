package com.example.rentalSystem.domain.book.schedule.dto.response;

import com.example.rentalSystem.domain.book.schedule.dto.request.ExcelScheduleRow;
import java.util.List;

public record ScheduleImportResponse(
    int totalRows,
    int successCount,
    int failedCount,
    List<RowError> errors
) {

  public static ScheduleImportResponse of(int total, int success, List<RowError> errors) {
    return new ScheduleImportResponse(total, success, total - success, errors);
  }

  public record RowError(int rowIndex, String reason, ExcelScheduleRow raw) {

  }
}