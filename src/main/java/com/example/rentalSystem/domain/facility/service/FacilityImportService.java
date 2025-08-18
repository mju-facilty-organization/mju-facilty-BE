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

@Service
@RequiredArgsConstructor
public class FacilityImportService {

    private final FacilityJpaRepository facilityJpaRepository;
    private final XlsFacilityParser parser;
    private final FacilityRowValidator validator;
    private final FacilityRowMapper mapper;
    private final FacilityTypeResolver typeResolver;

    @Transactional
    public FacilityImportResponseDto importFacilities(MultipartFile file, boolean dryRun, boolean overwrite) {
        int total = 0;
        int success = 0;
        int skipped = 0;
        List<String> warnings = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        List<FacilityRowDto> rows = parser.parse(file);
        total = rows.size();

        for (int i = 0; i < rows.size(); i++) {
            int rowNo = i + 2; // 헤더 다음이 2행
            FacilityRowDto row = rows.get(i);

            // 1) 1차 검증(필수값)
            List<String> rowErrors = validator.validate(row);
            if (!rowErrors.isEmpty()) {
                errors.add("#" + rowNo + " " + String.join(", ", rowErrors));
                skipped++;
                continue;
            }

            try {
                // 2) 업서트 키: (facilityType, facilityNumber)
                FacilityType keyType = typeResolver.resolveLoose(row.getFacilityTypeKo())
                        .orElseThrow(() -> new IllegalArgumentException("시설유형 매핑 실패: " + row.getFacilityTypeKo()));

                var existingOpt = facilityJpaRepository
                        .findByFacilityTypeAndFacilityNumber(keyType, row.getFacilityNumber());

                if (existingOpt.isPresent()) {
                    if (!overwrite) {
                        warnings.add("#" + rowNo + " 기존 시설 스킵: " + keyType.getValue() + "-" + row.getFacilityNumber());
                        skipped++;
                        continue;
                    }
                    // 갱신
                    Facility existing = existingOpt.get();
                    mapper.applyToExisting(existing, row);
                    if (!dryRun) facilityJpaRepository.save(existing);
                    success++;
                    warnings.add("#" + rowNo + " 기존 시설 갱신됨: " + keyType.getValue() + "-" + row.getFacilityNumber());
                } else {
                    // 신규
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