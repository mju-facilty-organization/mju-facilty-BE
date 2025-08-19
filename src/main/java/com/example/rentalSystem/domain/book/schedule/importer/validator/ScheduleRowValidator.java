package com.example.rentalSystem.domain.book.schedule.importer.validator;

import com.example.rentalSystem.domain.book.schedule.dto.request.ExcelScheduleRow;
import com.example.rentalSystem.domain.book.schedule.importer.exception.InvalidScheduleRowException;

public class ScheduleRowValidator {

  public void validate(ExcelScheduleRow row, int rowIndex) {
    if (isBlank(row.facilityName()) || isBlank(row.building())) {
      throw new InvalidScheduleRowException(rowIndex, "시설/건물 필수", row);
    }
    if (isBlank(row.day()) || isBlank(row.startTime()) || isBlank(row.endTime())) {
      throw new InvalidScheduleRowException(rowIndex, "요일/시간 필수", row);
    }
    if (row.capacity() != null && row.capacity() < 0) {
      throw new InvalidScheduleRowException(rowIndex, "수용인원 음수 불가", row);
    }
  }

  private boolean isBlank(String s) {
    return s == null || s.trim().isEmpty();
  }
}