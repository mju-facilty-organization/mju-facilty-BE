package com.example.rentalSystem.global.excel;

import java.time.LocalTime;
import java.util.Locale;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

@Component
public class RowReader {

  private final DataFormatter fmt = new DataFormatter(Locale.KOREA, true);
  private final TimeNormalizer timeNormalizer;

  public RowReader(TimeNormalizer timeNormalizer) {
    this.timeNormalizer = timeNormalizer;
  }

  public String getString(Cell cell) {
    if (cell == null) {
      return null;
    }
    String s = fmt.formatCellValue(cell);
    if (s == null) {
      return null;
    }
    s = s.trim();
    return s.isEmpty() ? null : s;
  }

  public Integer getInteger(Cell cell) {
    if (cell == null) {
      return null;
    }
    switch (cell.getCellType()) {
      case NUMERIC -> {
        return (int) Math.round(cell.getNumericCellValue());
      }
      case STRING -> {
        String s = cell.getStringCellValue();
        if (s == null) {
          return null;
        }
        s = s.trim();
        if (s.isEmpty()) {
          return null;
        }
        try {
          return Integer.parseInt(s);
        } catch (NumberFormatException e) {
          return null;
        }
      }
      default -> {
        return null;
      }
    }
  }

  public LocalTime getLocalTime(Cell cell) {
    if (cell == null) {
      return null;
    }
    switch (cell.getCellType()) {
      case NUMERIC -> {
        return timeNormalizer.toLocalTime(cell.getNumericCellValue());
      }
      case STRING -> {
        String s = cell.getStringCellValue();
        return timeNormalizer.toLocalTime(s);
      }
      default -> {
        return null;
      }
    }
  }

  public boolean isBlankRow(Row row) {
    for (Cell cell : row) {
      String v = fmt.formatCellValue(cell);
      if (v != null && !v.trim().isEmpty()) {
        return false;
      }
    }
    return true;
  }
}