package com.example.rentalSystem.domain.book.schedule.service;

import com.example.rentalSystem.domain.book.schedule.dto.request.ExcelScheduleRow;
import com.example.rentalSystem.domain.book.schedule.dto.response.ScheduleImportResponse;
import com.example.rentalSystem.domain.book.schedule.dto.response.ScheduleImportResponse.RowError;
import com.example.rentalSystem.domain.book.schedule.implement.ScheduleRemover;
import com.example.rentalSystem.domain.book.schedule.importer.exception.InvalidScheduleRowException;
import com.example.rentalSystem.domain.book.schedule.importer.mapper.ScheduleRowMapper;
import com.example.rentalSystem.domain.book.schedule.importer.parser.XlsScheduleParser;
import com.example.rentalSystem.domain.book.schedule.importer.validator.ScheduleRowValidator;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.implement.FacilityImpl;
import com.example.rentalSystem.global.exception.custom.CustomException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

// com.example.rentalSystem.domain.book.schedule.service.ScheduleImportService

@Service
@RequiredArgsConstructor
public class ScheduleImportService {

  private final XlsScheduleParser parser;
  private final ScheduleRowMapper mapper;
  private final ScheduleService scheduleService;
  private final FacilityImpl facilityImpl;

  private final ScheduleRowValidator validator = new ScheduleRowValidator();
  private final Map<String, Facility> facilityCache = new ConcurrentHashMap<>();

  private final ScheduleRemover scheduleRemover;

  @Transactional
  public ScheduleImportResponse importExcel(
      MultipartFile file, LocalDate validStart, LocalDate validEnd,
      boolean overwrite
  ) {
    List<RowError> errors = new ArrayList<>();
    int success = 0;

    try (InputStream in = file.getInputStream()) {
      List<ExcelScheduleRow> rows = parser.parse(in);

      record Key(Long facilityId, String organization) {

      }
      Map<Key, List<ExcelScheduleRow>> grouped = new LinkedHashMap<>();

      for (int i = 0; i < rows.size(); i++) {
        ExcelScheduleRow row = rows.get(i);
        int rowIndex = i + 2;

        try {
          validator.validate(row, rowIndex);

          Facility facility = facilityCache.computeIfAbsent(
              row.facilityName(),
              num -> facilityImpl.findByFacilityNumber(num)
          );

          Key key = new Key(facility.getId(), row.courseCode());
          grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(row);

        } catch (InvalidScheduleRowException e) {
          errors.add(new RowError(e.getRowIndex(), e.getMessage(), e.getRaw()));
        } catch (CustomException e) {
          errors.add(new RowError(rowIndex, e.getMessage(), row));
        } catch (Exception e) {
          errors.add(new RowError(rowIndex, e.getMessage(), row));
        }
      }

      for (Map.Entry<Key, List<ExcelScheduleRow>> entry : grouped.entrySet()) {
        Key key = entry.getKey();
        List<ExcelScheduleRow> groupRows = entry.getValue();

        if (overwrite) {
          scheduleRemover.deleteFacilityOrganizationRange(
              key.facilityId(), key.organization(), validStart, validEnd
          );
        }

        for (ExcelScheduleRow row : groupRows) {
          try {
            var req = mapper.toCreateRequest(row, key.facilityId(), validStart, validEnd);
            // 기존 ScheduleService는 겹침 검증을 수행.
            // overwrite=true로 미리 지웠으니, "기존 동일 강좌/시설"과의 충돌은 사라짐.
            scheduleService.createSchedule(req);
            success++;
          } catch (CustomException e) {
            errors.add(new RowError(-1, e.getMessage(), row)); // 그룹 삽입 중 예외
          } catch (Exception e) {
            errors.add(new RowError(-1, e.getMessage(), row));
          }
        }
      }

      return ScheduleImportResponse.of(rows.size(), success, errors);

    } catch (IOException e) {
      throw new RuntimeException("엑셀 파일 읽기 실패: " + e.getMessage(), e);
    }
  }
}