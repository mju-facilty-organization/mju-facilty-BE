package com.example.rentalSystem.domain.book.schedule.service;

import com.example.rentalSystem.domain.book.schedule.dto.request.CreateRegularScheduleRequest;
import com.example.rentalSystem.domain.book.schedule.dto.request.ExcelScheduleRow;
import com.example.rentalSystem.domain.book.schedule.dto.request.ScheduleRequest;
import com.example.rentalSystem.domain.book.schedule.dto.response.ScheduleImportResponse;
import com.example.rentalSystem.domain.book.schedule.dto.response.ScheduleImportResponse.RowError;
import com.example.rentalSystem.domain.book.schedule.implement.ScheduleRemover;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.type.ScheduleType;
import com.example.rentalSystem.domain.facility.implement.FacilityImpl;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.importer.ImportContext;
import com.example.rentalSystem.global.importer.ImportManager;
import com.example.rentalSystem.global.importer.ImportParseResult;
import com.example.rentalSystem.global.importer.ImportType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ScheduleImportService {

  private final ImportManager importManager;
  private final ScheduleService scheduleService;
  private final ScheduleRemover scheduleRemover;
  private final FacilityImpl facilityImpl;
  private final TransactionTemplate txTemplate;

  // facilityNumber(=강의실명) → Facility 캐시
  private final Map<String, Facility> facilityCache = new ConcurrentHashMap<>();

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public ScheduleImportResponse importExcel(
      MultipartFile file, LocalDate validStart, LocalDate validEnd, boolean overwrite
  ) {
    AtomicInteger success = new AtomicInteger();
    List<RowError> errors = new ArrayList<>();

    // 1) 엑셀 파싱
    ImportContext ctx = ImportContext.builder()
        .validStartDate(validStart)
        .validEndDate(validEnd)
        .build();

    ImportParseResult<ExcelScheduleRow> parsed =
        importManager.importExcel(file, ImportType.TIMETABLE, ctx);

    List<ExcelScheduleRow> rows = parsed.getItems();

    // 파서 단계 에러(필수값 누락 등) → field를 그대로 노출
    parsed.getErrors().forEach(e ->
        errors.add(new RowError(e.getRowIndex(), e.getField(), e.getMessage(), null))
    );

    // 2) (facilityId, organization[=courseCode]) 그룹핑
    record Key(Long facilityId, String organization) {

    }
    Map<Key, List<ExcelScheduleRow>> grouped = new LinkedHashMap<>();

    for (int i = 0; i < rows.size(); i++) {
      ExcelScheduleRow row = rows.get(i);
      int rowIndex = i + 2; // 헤더 다음 행 = 2

      try {
        Facility facility = facilityCache.computeIfAbsent(
            row.facilityName(), // == facilityNumber
            num -> facilityImpl.findByFacilityNumber(num)
        );
        Key key = new Key(facility.getId(), row.courseCode());
        grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(row);

      } catch (CustomException e) {
        // 시설을 못 찾은 경우: field를 "강의실명"으로 표기
        errors.add(new RowError(rowIndex, "강의실명", e.getMessage(), row));
      } catch (Exception e) {
        errors.add(new RowError(rowIndex, null, e.getMessage(), row));
      }
    }

    // 3) 그룹별 처리
    for (Map.Entry<Key, List<ExcelScheduleRow>> entry : grouped.entrySet()) {
      Key key = entry.getKey();
      List<ExcelScheduleRow> groupRows = entry.getValue();

      // overwrite면 기존 데이터 삭제 (그룹 단위 트랜잭션)
      if (overwrite) {
        txTemplate.executeWithoutResult(status -> {
          try {
            scheduleRemover.deleteFacilityOrganizationRange(
                key.facilityId(), key.organization(), validStart, validEnd
            );
          } catch (CustomException ce) {
            errors.add(new RowError(-1, "delete", ce.getMessage(), null));
            status.setRollbackOnly();
          } catch (Exception ex) {
            errors.add(new RowError(-1, "delete", ex.getMessage(), null));
            status.setRollbackOnly();
          }
        });
      }

      // 행 단위 삽입 (각 행은 독립 트랜잭션)
      for (ExcelScheduleRow row : groupRows) {
        txTemplate.executeWithoutResult(status -> {
          try {
            CreateRegularScheduleRequest req =
                toCreateRequest(row, key.facilityId(), validStart, validEnd);
            scheduleService.createSchedule(req);
            success.incrementAndGet();
          } catch (CustomException ce) {
            errors.add(new RowError(-1, "createSchedule", ce.getMessage(), row));
            status.setRollbackOnly();
          } catch (Exception ex) {
            errors.add(new RowError(-1, "createSchedule", ex.getMessage(), row));
            status.setRollbackOnly();
          }
        });
      }
    }

    return ScheduleImportResponse.of(parsed.getTotalRows(), success.get(), errors);
  }

  private CreateRegularScheduleRequest toCreateRequest(
      ExcelScheduleRow row, Long facilityId, LocalDate validStart, LocalDate validEnd
  ) {
    ScheduleRequest sr = new ScheduleRequest(
        com.example.rentalSystem.domain.book.schedule.importer.util.DayOfWeekResolver.resolve(row.day()),
        LocalTime.parse(row.startTime()),
        LocalTime.parse(row.endTime()),
        row.subject(),
        row.professor(),
        row.capacity()
    );

    return new CreateRegularScheduleRequest(
        row.courseCode(),               // organization = 강좌번호
        ScheduleType.CLASS,             // 고정
        facilityId,
        List.of(sr),
        validStart,
        validEnd
    );
  }
}