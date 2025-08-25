package com.example.rentalSystem.domain.book.schedule.importer;

import com.example.rentalSystem.domain.book.schedule.dto.request.ExcelScheduleRow;
import com.example.rentalSystem.global.excel.ErrorCollector;
import com.example.rentalSystem.global.excel.HeaderMapper;
import com.example.rentalSystem.global.excel.RowReader;
import com.example.rentalSystem.global.excel.TimeNormalizer;
import com.example.rentalSystem.global.importer.ExcelImporter;
import com.example.rentalSystem.global.importer.ImportContext;
import com.example.rentalSystem.global.importer.ImportError;
import com.example.rentalSystem.global.importer.ImportParseResult;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

@Component
public class ScheduleExcelImporter implements ExcelImporter<ExcelScheduleRow> {

  private static final List<String> REQUIRED_HEADERS = List.of(
      "강의실명", "위치(건물명)", "강좌번호", "요일", "시작시간", "종료시간"
  );
  private static final List<String> OPTIONAL_HEADERS = List.of(
      "과목명", "담당교수", "수용인원"
  );

  private static final String H_FACILITY = "강의실명";
  private static final String H_BUILDING = "위치(건물명)";
  private static final String H_COURSE = "강좌번호";
  private static final String H_DAY = "요일";
  private static final String H_START = "시작시간";
  private static final String H_END = "종료시간";
  private static final String H_SUBJECT = "과목명";
  private static final String H_PROF = "담당교수";
  private static final String H_CAP = "수용인원";

  private final HeaderMapper headerMapper;
  private final RowReader rowReader;
  private final TimeNormalizer timeNormalizer;

  public ScheduleExcelImporter(HeaderMapper headerMapper, RowReader rowReader, TimeNormalizer timeNormalizer) {
    this.headerMapper = headerMapper;
    this.rowReader = rowReader;
    this.timeNormalizer = timeNormalizer;
  }

  @Override
  public boolean supports(Workbook wb) {
    try {
      if (wb == null || wb.getNumberOfSheets() == 0) {
        return false;
      }
      Sheet sheet = wb.getSheetAt(0);
      if (sheet == null) {
        return false;
      }

      int headerRowIdx = headerMapper.headerRowIndexDetect(sheet, sheet.getFirstRowNum());
      Row header = sheet.getRow(headerRowIdx);
      if (header == null) {
        return false;
      }

      Map<String, Integer> colMap = new HashMap<>();
      DataFormatter fmt = new DataFormatter(Locale.KOREA, true);
      for (Cell cell : header) {
        String h = fmt.formatCellValue(cell);
        if (h != null && !h.trim().isEmpty()) {
          colMap.put(h.trim(), cell.getColumnIndex());
        }
      }

      var FACILITY_KEYS = List.of("강의실명", "강의실", "시설명", "호실", "강의실번호");
      var BUILDING_KEYS = List.of("건물명", "위치(건물명)", "건물", "동", "위치");
      var COURSE_KEYS = List.of("강좌번호", "과목번호", "수업번호", "학수번호");
      var DAY_KEYS = List.of("요일", "요일명");
      var START_KEYS = List.of("시작시간", "시작", "수업시작", "시작시각");
      var END_KEYS = List.of("종료시간", "종료", "수업종료", "종료시각");

      boolean hasFacility = colMap.keySet().stream().anyMatch(FACILITY_KEYS::contains);
      boolean hasBuilding = colMap.keySet().stream().anyMatch(BUILDING_KEYS::contains);
      boolean hasCourse = colMap.keySet().stream().anyMatch(COURSE_KEYS::contains);
      boolean hasDay = colMap.keySet().stream().anyMatch(DAY_KEYS::contains);
      boolean hasStart = colMap.keySet().stream().anyMatch(START_KEYS::contains);
      boolean hasEnd = colMap.keySet().stream().anyMatch(END_KEYS::contains);

      if (hasFacility && hasBuilding && hasCourse && hasDay && hasStart && hasEnd) {
        return true;
      }

      Row firstDataRow = sheet.getRow(headerRowIdx + 1);
      if (firstDataRow != null) {
        short last = firstDataRow.getLastCellNum();
        return last >= 6;
      }
      return false;

    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public ImportParseResult<ExcelScheduleRow> parse(Workbook wb, ImportContext ctx, ErrorCollector errors) {
    Sheet sheet = wb.getSheetAt(0);

    int headerRowIdx = sheet.getFirstRowNum();
    Row header = sheet.getRow(headerRowIdx);
    Map<String, Integer> col = headerMapper.map(header, REQUIRED_HEADERS, OPTIONAL_HEADERS);

    List<ExcelScheduleRow> out = new ArrayList<>();
    List<ImportError> err = new ArrayList<>();

    int totalRows = 0;

    for (int r = headerRowIdx + 1; r <= sheet.getLastRowNum(); r++) {
      Row row = sheet.getRow(r);
      if (row == null || rowReader.isBlankRow(row)) {
        continue;
      }
      totalRows++;

      String facilityName = get(row, col.get(H_FACILITY));
      String building = get(row, col.get(H_BUILDING));
      String courseCode = get(row, col.get(H_COURSE));
      String dayRaw = get(row, col.get(H_DAY));

      LocalTime startLt = getTime(row, col.get(H_START));
      LocalTime endLt = getTime(row, col.get(H_END));

      String subject = get(row, col.getOrDefault(H_SUBJECT, -1));
      String professor = get(row, col.getOrDefault(H_PROF, -1));
      Integer capacity = getInt(row, col.getOrDefault(H_CAP, -1));

      boolean rowHasError = false;
      rowHasError |= mustExist(errors, err, r, H_FACILITY, facilityName);
      rowHasError |= mustExist(errors, err, r, H_BUILDING, building);
      rowHasError |= mustExist(errors, err, r, H_COURSE, courseCode);
      rowHasError |= mustExist(errors, err, r, H_DAY, dayRaw);
      rowHasError |= mustExist(errors, err, r, H_START, startLt);
      rowHasError |= mustExist(errors, err, r, H_END, endLt);
      rowHasError |= mustExist(errors, err, r, H_SUBJECT, subject);
      rowHasError |= mustExist(errors, err, r, H_PROF, professor);
      rowHasError |= mustExist(errors, err, r, H_CAP, capacity);

      if (startLt != null && endLt != null && !endLt.isAfter(startLt)) {
        addErr(errors, err, r, H_END, "종료시간은 시작시간보다 이후여야 합니다.", timeNormalizer.formatHHmm(endLt));
        rowHasError = true;
      }
      if (capacity != null && capacity < 0) {
        addErr(errors, err, r, H_CAP, "수용인원은 음수가 될 수 없습니다.", String.valueOf(capacity));
        rowHasError = true;
      }

      if (rowHasError) {
        continue;
      }

      String start = timeNormalizer.formatHHmm(startLt);
      String end = timeNormalizer.formatHHmm(endLt);

      List<String> days = splitDays(dayRaw);

      if (days.isEmpty()) {
        addErr(errors, err, r, H_DAY, "요일을 해석할 수 없습니다.", dayRaw);
        continue;
      }

      for (String oneDay : days) {
        ExcelScheduleRow dto = new ExcelScheduleRow(
            trimOrNull(facilityName),
            trimOrNull(building),
            trimOrNull(courseCode),
            trimOrNull(oneDay),
            start,
            end,
            trimOrNull(subject),
            trimOrNull(professor),
            capacity
        );
        out.add(dto);
      }
    }

    return new ImportParseResult<>(out, err, totalRows, Map.of());
  }

  private String get(Row row, Integer idx) {
    if (idx == null || idx < 0) {
      return null;
    }
    return rowReader.getString(row.getCell(idx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL));
  }

  private Integer getInt(Row row, Integer idx) {
    if (idx == null || idx < 0) {
      return null;
    }
    return rowReader.getInteger(row.getCell(idx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL));
  }

  private LocalTime getTime(Row row, Integer idx) {
    if (idx == null || idx < 0) {
      return null;
    }
    return rowReader.getLocalTime(row.getCell(idx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL));
  }

  private boolean mustExist(ErrorCollector ec, List<ImportError> err, int rowIndex, String field, Object val) {
    if (val == null || (val instanceof String s && s.trim().isEmpty())) {
      addErr(ec, err, rowIndex, field, "필수값 누락", null);
      return true;
    }
    return false;
  }

  private void addErr(ErrorCollector ec, List<ImportError> err, int rowIndex,
      String field, String message, String raw) {
    ec.add(rowIndex, field, message, raw, null);
    err.add(new ImportError(rowIndex, field, message, raw, null));
  }

  private String trimOrNull(String s) {
    if (s == null) {
      return null;
    }
    String t = s.trim();
    return t.isEmpty() ? null : t;
  }

  private List<String> splitDays(String raw) {
    if (raw == null) {
      return List.of();
    }
    String s = raw.trim();

    String[] tokens = s.split("[/|,·•‧‥∙\\s]+"); // / , · • ‧ ‥ ∙ 공백 등
    List<String> out = new ArrayList<>();

    for (String tk : tokens) {
      String t = tk.trim();
      if (t.isEmpty()) {
        continue;
      }

      if (t.length() >= 2 && isKoreanDaysOnly(t)) {
        for (char ch : t.toCharArray()) {
          out.add(String.valueOf(ch));
        }
      } else {
        out.add(t);
      }
    }
    return out;
  }

  private boolean isKoreanDaysOnly(String s) {
    return s.matches("^[월화수목금토일]+$");
  }
}