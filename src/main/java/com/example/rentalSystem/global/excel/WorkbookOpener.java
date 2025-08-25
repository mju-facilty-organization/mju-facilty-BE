package com.example.rentalSystem.global.excel;

import java.io.InputStream;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

@Component
public class WorkbookOpener {

  public Workbook open(InputStream in) {
    try {
      return WorkbookFactory.create(in);
    } catch (Exception e) {
      throw new ExcelImportException("엑셀 파일을 열 수 없습니다.", e);
    }
  }

  public Sheet firstNonEmptySheet(Workbook wb) {
    for (int i = 0; i < wb.getNumberOfSheets(); i++) {
      Sheet s = wb.getSheetAt(i);
      if (s != null && s.getLastRowNum() >= s.getFirstRowNum()) {
        return s;
      }
    }
    throw new ExcelImportException("유효한 시트를 찾지 못했습니다.");
  }

  public Sheet getSheetByNameOrFirst(Workbook wb, String preferredName) {
    if (preferredName != null) {
      Sheet byName = wb.getSheet(preferredName);
      if (byName != null) {
        return byName;
      }
    }
    return firstNonEmptySheet(wb);
  }
}