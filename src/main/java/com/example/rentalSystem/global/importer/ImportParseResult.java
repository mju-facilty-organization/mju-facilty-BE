package com.example.rentalSystem.global.importer;

import java.util.List;
import java.util.Map;

public class ImportParseResult<T> {

  private final List<T> items;
  private final List<ImportError> errors;
  private final int totalRows;

  public ImportParseResult(List<T> items, List<ImportError> errors, int totalRows, Map<String, Object> meta) {
    this.items = items;
    this.errors = errors;
    this.totalRows = totalRows;
  }

  public List<T> getItems() {
    return items;
  }

  public List<ImportError> getErrors() {
    return errors;
  }

  public int getTotalRows() {
    return totalRows;
  }

  public static <T> ImportParseResult<T> of(List<T> items, List<ImportError> errors, int totalRows) {
    return new ImportParseResult<>(items, errors, totalRows, Map.of());
  }
}