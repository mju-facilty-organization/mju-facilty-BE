package com.example.rentalSystem.global.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ErrorCollector {

  public static final class Entry {

    public final int rowIndex;
    public final String field;
    public final String message;
    public final String rawValue;
    public final Integer columnIndex;

    public Entry(int rowIndex, String field, String message, String rawValue, Integer columnIndex) {
      this.rowIndex = rowIndex;
      this.field = field;
      this.message = message;
      this.rawValue = rawValue;
      this.columnIndex = columnIndex;
    }
  }

  private final List<Entry> entries = new ArrayList<>();

  public void add(int rowIndex, String field, String message, String rawValue, Integer columnIndexNullable) {
    entries.add(new Entry(rowIndex, field, message, rawValue, columnIndexNullable));
  }

  public boolean hasErrors() {
    return !entries.isEmpty();
  }

  public List<Entry> entries() {
    return List.copyOf(entries);
  }
  
  public <T> List<T> map(Function<Entry, T> mapper) {
    return entries.stream().map(mapper).toList();
  }
}