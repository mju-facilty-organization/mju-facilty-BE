package com.example.rentalSystem.global.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

@Component
public class HeaderMapper {

  public Map<String, Integer> map(Row headerRow, List<String> required, List<String> optional) {
    if (headerRow == null) {
      throw new ExcelImportException("헤더 행이 비어 있습니다.");
    }

    DataFormatter fmt = new DataFormatter(Locale.KOREA, true);
    Map<String, Integer> map = new HashMap<>();
    short last = headerRow.getLastCellNum();
    for (int c = 0; c < last; c++) {
      Cell cell = headerRow.getCell(c, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
      if (cell == null) {
        continue;
      }
      String h = fmt.formatCellValue(cell);
      if (h == null) {
        continue;
      }
      h = h.trim();
      if (h.isEmpty()) {
        continue;
      }
      map.put(h, c);
    }
    ensureRequired(map, required);
    return map;
  }

  public void ensureRequired(Map<String, Integer> map, List<String> required) {
    if (required == null || required.isEmpty()) {
      return;
    }
    List<String> missing = required.stream()
        .filter(h -> !map.containsKey(h))
        .collect(Collectors.toList());
    if (!missing.isEmpty()) {
      throw new ExcelImportException("필수 헤더 누락: " + String.join(", ", missing));
    }
  }
  
  public int headerRowIndexDetect(Sheet sheet, int fallbackIndex) {
    int first = sheet.getFirstRowNum();
    int last = sheet.getLastRowNum();
    for (int r = first; r <= last; r++) {
      Row row = sheet.getRow(r);
      if (row == null) {
        continue;
      }
      if (!isBlankRow(row)) {
        return r;
      }
    }
    return fallbackIndex;
  }

  private boolean isBlankRow(Row row) {
    DataFormatter fmt = new DataFormatter(Locale.KOREA, true);
    for (Cell cell : row) {
      String v = fmt.formatCellValue(cell);
      if (v != null && !v.trim().isEmpty()) {
        return false;
      }
    }
    return true;
  }
}