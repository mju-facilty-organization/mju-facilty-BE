package com.example.rentalSystem.global.importer;

import com.example.rentalSystem.global.excel.ErrorCollector;
import com.example.rentalSystem.global.excel.ExcelImportException;
import com.example.rentalSystem.global.excel.WorkbookOpener;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImportManager {

  private final WorkbookOpener opener;
  private final List<ExcelImporter<?>> importers;

  public ImportManager(WorkbookOpener opener, List<ExcelImporter<?>> importers) {
    this.opener = opener;
    this.importers = importers;
    System.out.println("[ImportManager] loaded importers: " +
        importers.stream().map(i -> i.getClass().getName()).toList());
  }

  @SuppressWarnings("unchecked")
  public <T> ImportParseResult<T> importExcel(MultipartFile file, ImportType type, ImportContext ctx) {
    try (InputStream in = file.getInputStream(); Workbook wb = opener.open(in)) {
      List<ExcelImporter<?>> candidates = importers.stream()
          .filter(imp -> safeSupports(imp, wb))
          .collect(Collectors.toList());

      if (candidates.isEmpty()) {
        throw new ExcelImportException("지원되는 임포터가 없습니다. 파일 포맷을 확인하세요.");
      }
      if (candidates.size() > 1 && type == ImportType.AUTO) {
        throw new ExcelImportException("여러 임포터가 동시에 매칭되었습니다. 파일 포맷을 명확히 하세요.");
      }

      ExcelImporter<?> importer = chooseByTypeOrSingle(candidates, type);

      ErrorCollector ec = new ErrorCollector();
      ImportParseResult<?> result = importer.parse(wb, ctx, ec);

      return (ImportParseResult<T>) result;

    } catch (ExcelImportException e) {
      throw e;
    } catch (Exception e) {
      throw new ExcelImportException("임포트 처리 중 오류가 발생했습니다.", e);
    }
  }

  private boolean safeSupports(ExcelImporter<?> imp, Workbook wb) {
    try {
      return imp.supports(wb);
    } catch (Exception e) {
      return false;
    }
  }

  private ExcelImporter<?> chooseByTypeOrSingle(List<ExcelImporter<?>> candidates, ImportType type) {
    if (type == ImportType.AUTO || candidates.size() == 1) {
      return candidates.get(0);
    }
    return candidates.get(0);
  }
}