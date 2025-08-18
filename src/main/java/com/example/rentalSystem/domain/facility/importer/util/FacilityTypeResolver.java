package com.example.rentalSystem.domain.facility.importer.util;

import com.example.rentalSystem.domain.facility.entity.type.FacilityType;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 문자열 → FacilityType 변환 유틸 (공백/대소문자/별칭 느슨 매칭)
 */
@Component
public class FacilityTypeResolver {

    public Optional<FacilityType> resolveLoose(String raw) {
        if (raw == null) return Optional.empty();
        String v = raw.replaceAll("\\s+", "").toLowerCase();
        return switch (v) {
            case "본관" -> Optional.of(FacilityType.MAIN_BUILDING);
            case "국제관" -> Optional.of(FacilityType.INTERNATIONAL_BUILDING);
            case "학생회관" -> Optional.of(FacilityType.STUDENT_HALL);
            case "mcc" -> Optional.of(FacilityType.MCC);
            case "도서관" -> Optional.of(FacilityType.LIBRARY);
            case "야외" -> Optional.of(FacilityType.OUTDOOR);
            default -> Optional.empty();
        };
    }

    /**
     * 엄격 매칭 (한글값 정확히 일치)
     */
    public FacilityType resolveStrict(String name) {
        return FacilityType.getInstanceByValue(name.trim());
    }
}