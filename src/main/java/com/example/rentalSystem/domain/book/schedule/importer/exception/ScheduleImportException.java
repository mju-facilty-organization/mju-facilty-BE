package com.example.rentalSystem.domain.book.schedule.importer.exception;

public class ScheduleImportException extends RuntimeException {

  public ScheduleImportException(String message, Throwable cause) {
    super(message, cause);
  }

  public ScheduleImportException(String message) {
    super(message);
  }
}