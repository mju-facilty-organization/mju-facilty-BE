// src/main/java/com/example/rentalSystem/global/importer/ImportManager.java
package com.example.rentalSystem.global.importer;

import com.example.rentalSystem.domain.book.schedule.importer.ScheduleExcelImporter;
import com.example.rentalSystem.domain.facility.importer.FacilityExcelImporter;
import com.example.rentalSystem.global.excel.ErrorCollector;
import com.example.rentalSystem.global.excel.ExcelImportException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ImportManager {

  private final FacilityExcelImporter facilityExcelImporter;
  private final ScheduleExcelImporter scheduleExcelImporter;

  @SuppressWarnings("unchecked")
  public <T> ImportParseResult<T> importExcel(MultipartFile file, ImportType type, ImportContext ctx) {
    try (InputStream in = file.getInputStream(); Workbook wb = WorkbookFactory.create(in)) {
      ErrorCollector ec = new ErrorCollector();
      return switch (type) {
        case FACILITY -> (ImportParseResult<T>) facilityExcelImporter.parse(wb, ctx, ec);
        case TIMETABLE -> (ImportParseResult<T>) scheduleExcelImporter.parse(wb, ctx, ec);
        default -> throw new ExcelImportException("지원하지 않는 ImportType: " + type);
      };
    } catch (Exception e) {
      throw new ExcelImportException("엑셀 임포트 실패: " + e.getMessage(), e);
    }
  }
}