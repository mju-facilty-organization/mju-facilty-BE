package com.example.rentalSystem.global.importer;

import com.example.rentalSystem.global.excel.ErrorCollector;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelImporter<T> {

  boolean supports(Workbook wb);
  
  ImportParseResult<T> parse(Workbook wb, ImportContext ctx, ErrorCollector errors);
}