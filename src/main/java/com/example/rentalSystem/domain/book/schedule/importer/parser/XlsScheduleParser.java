package com.example.rentalSystem.domain.book.schedule.importer.parser;

import com.example.rentalSystem.domain.book.schedule.dto.request.ExcelScheduleRow;
import com.example.rentalSystem.domain.book.schedule.importer.exception.ScheduleImportException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

@Component
public class XlsScheduleParser {

  private static final DateTimeFormatter HHMM = DateTimeFormatter.ofPattern("HH:mm");

  public List<ExcelScheduleRow> parse(InputStream in) {
    try (Workbook wb = WorkbookFactory.create(in)) {
      Sheet sheet = wb.getSheetAt(0);
      List<ExcelScheduleRow> rows = new ArrayList<>();

      // 0행 = 헤더, 1행부터 데이터
      for (int r = 1; r <= sheet.getLastRowNum(); r++) {
        Row row = sheet.getRow(r);
        if (row == null) {
          continue;
        }

        String facilityName = getStringCell(row.getCell(0));
        String building = getStringCell(row.getCell(1));
        String courseCode = getStringCell(row.getCell(2));
        String day = getStringCell(row.getCell(3));

        // 시간 컬럼은 전용 파서로
        String startTime = getTimeString(row.getCell(4));
        String endTime = getTimeString(row.getCell(5));

        String subject = getStringCell(row.getCell(6));
        String professor = getStringCell(row.getCell(7));
        Integer capacity = getIntegerCell(row.getCell(8));

        rows.add(new ExcelScheduleRow(
            nullSafeTrim(facilityName),
            nullSafeTrim(building),
            nullSafeTrim(courseCode),
            nullSafeTrim(day),
            startTime, // 이미 "HH:mm"
            endTime,   // 이미 "HH:mm"
            nullSafeTrim(subject),
            nullSafeTrim(professor),
            capacity
        ));
      }
      return rows;
    } catch (Exception e) {
      throw new ScheduleImportException("엑셀 파싱 실패", e);
    }
  }

  private String nullSafeTrim(String s) {
    return s == null ? null : s.trim();
  }

  private String getStringCell(Cell cell) {
    if (cell == null) {
      return null;
    }
    return switch (cell.getCellType()) {
      case STRING -> cell.getStringCellValue();
      case NUMERIC -> {
        double v = cell.getNumericCellValue();
        // 일반 숫자는 문자열로 반환 (시간은 getTimeString에서 처리)
        if (v == Math.rint(v)) {
          yield String.valueOf((long) v);
        } else {
          yield String.valueOf(v);
        }
      }
      case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
      default -> null;
    };
  }

  private Integer getIntegerCell(Cell cell) {
    if (cell == null) {
      return null;
    }
    return switch (cell.getCellType()) {
      case NUMERIC -> (int) Math.round(cell.getNumericCellValue());
      case STRING -> {
        String s = cell.getStringCellValue().trim();
        try {
          yield Integer.parseInt(s);
        } catch (NumberFormatException e) {
          yield null;
        }
      }
      default -> null;
    };
  }

  // XlsScheduleParser.java (교체)
  private String getTimeString(Cell cell) {
    if (cell == null) {
      return null;
    }

    switch (cell.getCellType()) {
      case STRING -> {
        String raw = cell.getStringCellValue().trim();
        return normalizeToHHmm(raw);
      }
      case NUMERIC -> {
        double v = cell.getNumericCellValue();

        // 엑셀 숫자 시간 처리:
        //  - 날짜+시간인 경우: 소수부(시간 부분)만 떼서 사용
        //  - 순수 시간(0~1)인 경우: 그대로 사용
        double frac = v % 1.0;
        if (frac < 0) {
          frac += 1.0; // 안전 처리
        }

        // 분수(하루=1.0) → 총 초수
        int totalSeconds = (int) Math.round(frac * 24 * 60 * 60);
        int hour = (totalSeconds / 3600) % 24;
        int minute = (totalSeconds % 3600) / 60;

        // HH:mm 문자열로 반환
        return String.format("%02d:%02d", hour, minute);
      }
      default -> {
        return null;
      }
    }
  }

  // "10:00", "1000", "930", "9:30", "9" 등 → "HH:mm"
  private String normalizeToHHmm(String raw) {
    if (raw == null || raw.isEmpty()) {
      return null;
    }
    String s = raw.trim();

    if (s.matches("^\\d{1,2}:\\d{2}$")) {
      LocalTime lt = LocalTime.parse(padHour(s), HHMM);
      return HHMM.format(lt);
    }

    if (s.matches("^\\d{1,4}$")) {
      int n = Integer.parseInt(s);
      int hour, minute;
      if (n < 100) {
        hour = n;
        minute = 0;
      } else {
        hour = n / 100;
        minute = n % 100;
      }
      LocalTime lt = LocalTime.of(hour, minute);
      return HHMM.format(lt);
    }

    throw new IllegalArgumentException("시간 포맷을 해석할 수 없습니다: " + raw);
  }

  private String padHour(String s) {
    if (s.matches("^\\d:\\d{2}$")) {
      return "0" + s;
    }
    return s;
  }
}