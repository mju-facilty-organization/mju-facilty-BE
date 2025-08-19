package com.example.rentalSystem.domain.book.schedule.importer.exception;

import com.example.rentalSystem.domain.book.schedule.dto.request.ExcelScheduleRow;

public class InvalidScheduleRowException extends RuntimeException {

  private final int rowIndex;
  private final ExcelScheduleRow raw;

  public InvalidScheduleRowException(int rowIndex, String message, ExcelScheduleRow raw) {
    super(message);
    this.rowIndex = rowIndex;
    this.raw = raw;
  }

  public int getRowIndex() {
    return rowIndex;
  }

  public ExcelScheduleRow getRaw() {
    return raw;
  }
}