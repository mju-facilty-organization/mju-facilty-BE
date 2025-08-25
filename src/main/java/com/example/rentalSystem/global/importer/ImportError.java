package com.example.rentalSystem.global.importer;

public class ImportError {

  private final int rowIndex;
  private final String field;
  private final String message;


  public ImportError(int rowIndex, String field, String message, String rawValue, Integer columnIndex) {
    this.rowIndex = rowIndex;
    this.field = field;
    this.message = message;
  }

  public int getRowIndex() {
    return rowIndex;
  }

  public String getField() {
    return field;
  }

  public String getMessage() {
    return message;
  }

}