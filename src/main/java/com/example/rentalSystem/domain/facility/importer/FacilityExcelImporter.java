// src/main/java/com/example/rentalSystem/domain/facility/importer/FacilityExcelImporter.java
package com.example.rentalSystem.domain.facility.importer;

import com.example.rentalSystem.domain.facility.dto.model.FacilityRowDto;
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
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FacilityExcelImporter implements ExcelImporter<FacilityRowDto> {

  private static final List<String> REQUIRED_HEADERS = List.of(
      "시설유형", "시설번호", "수용인원", "시작시간", "종료시간", "단과대학"
  );
  private static final List<String> OPTIONAL_HEADERS = List.of(
      "이용범위", "지원시설", "사용가능여부"
  );

  private static final String H_TYPE = "시설유형";
  private static final String H_NUMBER = "시설번호";
  private static final String H_CAP = "수용인원";
  private static final String H_START = "시작시간";
  private static final String H_END = "종료시간";
  private static final String H_COLLEGE = "단과대학";
  private static final String H_RANGE = "이용범위";
  private static final String H_SUPPORTS = "지원시설";
  private static final String H_AVAILABLE = "사용가능여부";

  private final HeaderMapper headerMapper;
  private final RowReader rowReader;
  private final TimeNormalizer timeNormalizer;

  @Override
  public boolean supports(Workbook wb) {
    if (wb == null || wb.getNumberOfSheets() == 0) {
      return false;
    }
    Sheet s = wb.getSheetAt(0);
    if (s == null) {
      return false;
    }

    int headerRowIdx = headerMapper.headerRowIndexDetect(s, s.getFirstRowNum());
    Row header = s.getRow(headerRowIdx);
    if (header == null) {
      return false;
    }

    // 필수 헤더들이 존재하면 true
    Map<String, Integer> col = headerMapper.map(header, REQUIRED_HEADERS, OPTIONAL_HEADERS);
    // map()에서 필수 없으면 예외이므로 여기까지 오면 true 취급
    return col != null;
  }

  @Override
  public ImportParseResult<FacilityRowDto> parse(Workbook wb, ImportContext ctx, ErrorCollector ec) {
    Sheet sheet = wb.getSheetAt(0);
    int headerRowIdx = sheet.getFirstRowNum();
    Row header = sheet.getRow(headerRowIdx);
    Map<String, Integer> col = headerMapper.map(header, REQUIRED_HEADERS, OPTIONAL_HEADERS);

    List<FacilityRowDto> out = new ArrayList<>();
    List<ImportError> errors = new ArrayList<>();
    int totalRows = 0;

    for (int r = headerRowIdx + 1; r <= sheet.getLastRowNum(); r++) {
      Row row = sheet.getRow(r);
      if (row == null || rowReader.isBlankRow(row)) {
        continue;
      }
      totalRows++;

      String type = get(row, col.get(H_TYPE));
      String number = get(row, col.get(H_NUMBER));
      String capRaw = get(row, col.get(H_CAP));

      LocalTime startLt = getTime(row, col.get(H_START));
      LocalTime endLt = getTime(row, col.get(H_END));

      String college = get(row, col.get(H_COLLEGE));
      String range = get(row, col.getOrDefault(H_RANGE, -1));
      String supports = get(row, col.getOrDefault(H_SUPPORTS, -1));
      String available = get(row, col.getOrDefault(H_AVAILABLE, -1));

      boolean rowHasError = false;
      rowHasError |= must(ec, errors, r, H_TYPE, type);
      rowHasError |= must(ec, errors, r, H_NUMBER, number);
      rowHasError |= must(ec, errors, r, H_CAP, capRaw);
      rowHasError |= must(ec, errors, r, H_START, startLt);
      rowHasError |= must(ec, errors, r, H_END, endLt);
      rowHasError |= must(ec, errors, r, H_COLLEGE, college);

      if (startLt != null && endLt != null && !endLt.isAfter(startLt)) {
        addErr(ec, errors, r, H_END, "종료시간은 시작시간보다 이후여야 합니다.", timeNormalizer.formatHHmm(endLt));
        rowHasError = true;
      }

      if (rowHasError) {
        continue;
      }

      FacilityRowDto dto = FacilityRowDto.builder()
          .facilityTypeKo(trim(type))
          .facilityNumber(trim(number))
          .capacityKo(trim(capRaw))
          .startTime(timeNormalizer.formatHColonMm(startLt)) // 9:00 / 09:00 모두 허용 → "H:mm"로 표준화
          .endTime(timeNormalizer.formatHColonMm(endLt))
          .collegeKo(trim(college))
          .allowedRangeKo(trim(range))
          .supportFacilitiesKo(trim(supports))
          .availableKo(trim(available))
          .build();

      out.add(dto);
    }

    return new ImportParseResult<>(out, errors, totalRows, Map.of());
  }

  private String get(Row row, Integer idx) {
    if (idx == null || idx < 0) {
      return null;
    }
    return rowReader.getString(row.getCell(idx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL));
  }

  private LocalTime getTime(Row row, Integer idx) {
    if (idx == null || idx < 0) {
      return null;
    }
    // 숫자/문자/시간형 모두 안전 파싱
    return rowReader.getLocalTime(row.getCell(idx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL));
  }

  private boolean must(ErrorCollector ec, List<ImportError> list, int r, String field, Object v) {
    if (v == null || (v instanceof String s && s.trim().isEmpty())) {
      addErr(ec, list, r, field, "필수값 누락", null);
      return true;
    }
    return false;
  }

  private void addErr(ErrorCollector ec, List<ImportError> list, int r, String field, String msg, String raw) {
    ec.add(r, field, msg, raw, null);
    list.add(new ImportError(r, field, msg, raw, null));
  }

  private String trim(String s) {
    if (s == null) {
      return null;
    }
    String t = s.trim();
    return t.isEmpty() ? null : t;
  }
}