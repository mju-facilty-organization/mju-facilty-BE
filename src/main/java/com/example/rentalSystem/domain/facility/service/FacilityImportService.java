// src/main/java/com/example/rentalSystem/domain/facility/service/FacilityImportService.java
package com.example.rentalSystem.domain.facility.service;

import static com.example.rentalSystem.domain.facility.importer.util.FacilityNumberNormalizer.normalize;

import com.example.rentalSystem.domain.facility.dto.model.FacilityRowDto;
import com.example.rentalSystem.domain.facility.dto.response.FacilityImportResponseDto;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.importer.mapper.FacilityRowMapper;
import com.example.rentalSystem.domain.facility.importer.util.FacilityTypeResolver;
import com.example.rentalSystem.domain.facility.reposiotry.FacilityJpaRepository;
import com.example.rentalSystem.global.importer.ImportContext;
import com.example.rentalSystem.global.importer.ImportManager;
import com.example.rentalSystem.global.importer.ImportParseResult;
import com.example.rentalSystem.global.importer.ImportType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FacilityImportService {

  private final FacilityJpaRepository facilityJpaRepository;
  private final FacilityRowMapper mapper;
  private final FacilityTypeResolver typeResolver;

  private final ImportManager importManager;

  @Transactional
  public FacilityImportResponseDto importFacilities(
      MultipartFile file,
      boolean dryRun,
      boolean overwrite,
      boolean duplicateAsError
  ) {
    int total = 0;
    int success = 0;
    int skipped = 0;
    List<String> warnings = new ArrayList<>();
    List<String> errors = new ArrayList<>();

    ImportContext ctx = ImportContext.builder().build();
    ImportParseResult<FacilityRowDto> parsed =
        importManager.importExcel(file, ImportType.FACILITY, ctx);

    total = parsed.getTotalRows();

    for (var e : parsed.getErrors()) {
      errors.add("#" + e.getRowIndex() + " [" + e.getField() + "] " + e.getMessage());
      skipped++;
    }

    // ★ 엑셀 내 중복 감지 (번호 → 최초 등장 행 번호)
    Map<String, Integer> seenNumbers = new HashMap<>();

    List<FacilityRowDto> rows = parsed.getItems();
    for (int i = 0; i < rows.size(); i++) {
      FacilityRowDto row = rows.get(i);
      int rowNo = i + 2; // 헤더 다음이 2행

      try {
        // (1) 시설번호 표준화
        String rawNo = row.getFacilityNumber();
        String normalizedNo = normalize(rawNo);
        if (!Objects.equals(rawNo, normalizedNo)) {
          row.setFacilityNumber(normalizedNo);
          warnings.add("#" + rowNo + " 시설번호 자동 보정: '" + rawNo + "' → '" + normalizedNo + "'");
        }

        // (2) 엑셀 내 중복 감지
        if (seenNumbers.containsKey(normalizedNo)) {
          int firstRow = seenNumbers.get(normalizedNo);
          String msg = "#" + rowNo + " 엑셀 내 중복 시설번호 스킵: "
              + normalizedNo + " (중복행: #" + firstRow + ")";
          if (duplicateAsError) {
            errors.add(msg);
          } else {
            warnings.add(msg);
          }
          skipped++;
          continue;
        } else {
          seenNumbers.put(normalizedNo, rowNo);
        }

        // (3) 시설유형 키 계산
        var keyType = typeResolver.resolveLoose(row.getFacilityTypeKo())
            .orElseThrow(() -> new IllegalArgumentException("시설유형 매핑 실패: " + row.getFacilityTypeKo()));
        String keyTypeStr = keyType.getValue().toUpperCase(Locale.ROOT);
        String keyNumber = row.getFacilityNumber();

        var existingOpt = facilityJpaRepository.findByFacilityNumber(keyNumber);

        if (existingOpt.isPresent()) {
          if (!overwrite) {
            warnings.add("#" + rowNo + " 기존 시설 스킵(overwrite=false): "
                + keyTypeStr + "-" + keyNumber);
            skipped++;
            continue;
          }
          Facility existing = existingOpt.get();
          mapper.applyToExisting(existing, row);
          if (!dryRun) {
            facilityJpaRepository.save(existing);
          }
          success++;
          warnings.add("#" + rowNo + " 기존 시설 갱신: "
              + keyTypeStr + "-" + keyNumber);
        } else {
          Facility fresh = mapper.toNewEntity(row);
          if (!dryRun) {
            facilityJpaRepository.save(fresh);
          }
          success++;
        }

      } catch (Exception e) {
        errors.add("#" + rowNo + " " + e.getMessage());
        skipped++;
      }
    }

    return new FacilityImportResponseDto(total, success, skipped, warnings, errors);
  }
}