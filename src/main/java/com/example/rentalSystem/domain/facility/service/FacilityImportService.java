package com.example.rentalSystem.domain.facility.service;

import com.example.rentalSystem.domain.facility.dto.model.FacilityRowDto;
import com.example.rentalSystem.domain.facility.dto.response.FacilityImportResponseDto;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.type.FacilityType;
import com.example.rentalSystem.domain.facility.importer.mapper.FacilityRowMapper;
import com.example.rentalSystem.domain.facility.importer.parser.XlsFacilityParser;
import com.example.rentalSystem.domain.facility.importer.util.FacilityTypeResolver;
import com.example.rentalSystem.domain.facility.importer.validator.FacilityRowValidator;
import com.example.rentalSystem.domain.facility.reposiotry.FacilityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.rentalSystem.domain.facility.importer.util.FacilityNumberNormalizer.normalize;

@Service
@RequiredArgsConstructor
public class FacilityImportService {

    private final FacilityJpaRepository facilityJpaRepository;
    private final XlsFacilityParser parser;
    private final FacilityRowValidator validator;
    private final FacilityRowMapper mapper;
    private final FacilityTypeResolver typeResolver;

    /**
     * XLS/XLSX 업로드:
     * - 번호 자동 표준화(S접두+대문자). 변경되면 warnings에 기록
     * - (facilityType, facilityNumber) 키로 업서트
     * - overwrite=false면 기존 레코드 스킵
     * - dryRun=true면 DB 미반영(리포트만)
     */
    @Transactional
    public FacilityImportResponseDto importFacilities(MultipartFile file, boolean dryRun, boolean overwrite) {
        int total = 0;
        int success = 0;
        int skipped = 0;
        List<String> warnings = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        // 파싱 (XlsFacilityParser가 런타임 예외로 래핑하므로 별도 IOException catch 불필요)
        List<FacilityRowDto> rows = parser.parse(file);
        total = rows.size();

        for (int i = 0; i < rows.size(); i++) {
            int rowNo = i + 2; // 헤더 다음 행 = 2행
            FacilityRowDto row = rows.get(i);

            // 1) 필수값 검증 (형식은 validator에서 느슨하게: 존재만 체크)
            List<String> rowErrors = validator.validate(row);
            if (!rowErrors.isEmpty()) {
                errors.add("#" + rowNo + " " + String.join(", ", rowErrors));
                skipped++;
                continue;
            }

            try {
                // 2) 시설번호 자동 표준화 (S 접두 + 대문자)
                String rawNo = row.getFacilityNumber();
                String normalizedNo = normalize(rawNo);
                if (!Objects.equals(rawNo, normalizedNo)) {
                    // DTO에 반영해서 매퍼/키조회 모두 표준화된 번호 사용
                    row.setFacilityNumber(normalizedNo);
                    warnings.add("#" + rowNo + " 시설번호 자동 보정: '" + rawNo + "' → '" + normalizedNo + "'");
                }

                // 3) 업서트 키 계산
                // ... 생략
// 3) 업서트 키 계산
                FacilityType keyType = typeResolver.resolveLoose(row.getFacilityTypeKo())
                        .orElseThrow(() -> new IllegalArgumentException("시설유형 매핑 실패: " + row.getFacilityTypeKo()));
                String keyNumber = row.getFacilityNumber(); // 이미 표준화됨

// (변경) 타입+번호 → 번호만
                var existingOpt = facilityJpaRepository.findByFacilityNumber(keyNumber);

                if (existingOpt.isPresent()) {
                    if (!overwrite) {
                        warnings.add("#" + rowNo + " 기존 시설 스킵: " + keyType.getValue() + "-" + keyNumber);
                        skipped++;
                        continue;
                    }
                    Facility existing = existingOpt.get();
                    mapper.applyToExisting(existing, row);
                    if (!dryRun) facilityJpaRepository.save(existing);
                    success++;
                    warnings.add("#" + rowNo + " 기존 시설 갱신됨: " + keyType.getValue() + "-" + keyNumber);
                } else {
                    Facility fresh = mapper.toNewEntity(row);
                    if (!dryRun) facilityJpaRepository.save(fresh);
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