package com.example.rentalSystem.domain.facility.importer.parser;

import com.example.rentalSystem.domain.facility.dto.model.FacilityRowDto;
import java.io.InputStream;
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
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * .xls / .xlsx 모두 지원 (WorkbookFactory 사용) 헤더는 1행 고정: 시설유형 | 시설번호 | 수용인원 | 시작시간 | 종료시간 | 단과대학 | 이용범위 | 지원시설 | 사용가능여부
 */
@Component
public class XlsFacilityParser {

    private static final String H_FACILITY_TYPE = "시설유형";
    private static final String H_FACILITY_NUMBER = "시설번호";
    private static final String H_CAPACITY = "수용인원";
    private static final String H_START = "시작시간";
    private static final String H_END = "종료시간";
    private static final String H_COLLEGE = "단과대학";
    private static final String H_RANGE = "이용범위";        // optional
    private static final String H_SUPPORTS = "지원시설";        // optional
    private static final String H_AVAILABLE = "사용가능여부";    // optional

    private static final List<String> REQUIRED = List.of(
        H_FACILITY_TYPE, H_FACILITY_NUMBER, H_CAPACITY, H_START, H_END, H_COLLEGE
    );

    public List<FacilityRowDto> parse(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return List.of();
        }

        try (InputStream in = file.getInputStream();
            Workbook wb = WorkbookFactory.create(in)) {  // xls/xlsx 둘 다 OK
            Sheet sheet = firstNonEmptySheet(wb);
            if (sheet == null) {
                return List.of();
            }

            int headerRowIdx = sheet.getFirstRowNum();
            Row header = sheet.getRow(headerRowIdx);
            if (header == null) {
                throw new IllegalArgumentException("헤더 행이 비어 있습니다.");
            }

            Map<String, Integer> colIndex = mapHeader(header);
            ensureRequired(colIndex);

            DataFormatter fmt = new DataFormatter(Locale.KOREA, true);
            List<FacilityRowDto> out = new ArrayList<>();
            for (int r = headerRowIdx + 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }
                if (isBlankRow(row, fmt)) {
                    continue;
                }

                FacilityRowDto dto = FacilityRowDto.builder()
                    .facilityTypeKo(get(fmt, row, colIndex.get(H_FACILITY_TYPE)))
                    .facilityNumber(get(fmt, row, colIndex.get(H_FACILITY_NUMBER)))
                    .capacityKo(get(fmt, row, colIndex.get(H_CAPACITY)))
                    .startTime(get(fmt, row, colIndex.get(H_START)))
                    .endTime(get(fmt, row, colIndex.get(H_END)))
                    .collegeKo(get(fmt, row, colIndex.get(H_COLLEGE)))
                    .allowedRangeKo(get(fmt, row, colIndex.getOrDefault(H_RANGE, -1)))
                    .supportFacilitiesKo(get(fmt, row, colIndex.getOrDefault(H_SUPPORTS, -1)))
                    .availableKo(get(fmt, row, colIndex.getOrDefault(H_AVAILABLE, -1)))
                    .build();

                out.add(dto);
            }
            return out;
        } catch (Exception e) {
            throw new RuntimeException("엑셀 파싱 실패: " + e.getMessage(), e);
        }
    }

    private static Sheet firstNonEmptySheet(Workbook wb) {
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet s = wb.getSheetAt(i);
            if (s != null && s.getLastRowNum() >= s.getFirstRowNum()) {
                return s;
            }
        }
        return null;
    }

    private static Map<String, Integer> mapHeader(Row headerRow) {
        Map<String, Integer> map = new HashMap<>();
        DataFormatter fmt = new DataFormatter(Locale.KOREA, true);
        short last = headerRow.getLastCellNum();
        for (int c = 0; c < last; c++) {
            Cell cell = headerRow.getCell(c, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell == null) {
                continue;
            }
            String h = fmt.formatCellValue(cell).trim();
            if (h.isBlank()) {
                continue;
            }
            map.put(h, c);
        }
        return map;
    }

    private static void ensureRequired(Map<String, Integer> colIndex) {
        List<String> missing = new ArrayList<>();
        for (String h : REQUIRED) {
            if (!colIndex.containsKey(h)) {
                missing.add(h);
            }
        }
        if (!missing.isEmpty()) {
            throw new IllegalArgumentException("필수 헤더 누락: " + String.join(", ", missing));
        }
    }

    private static boolean isBlankRow(Row row, DataFormatter fmt) {
        for (Cell cell : row) {
            String v = fmt.formatCellValue(cell);
            if (v != null && !v.trim().isBlank()) {
                return false;
            }
        }
        return true;
    }

    private static String get(DataFormatter fmt, Row row, Integer idx) {
        if (idx == null || idx < 0) {
            return null;
        }
        Cell cell = row.getCell(idx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) {
            return null;
        }
        String s = fmt.formatCellValue(cell);
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}