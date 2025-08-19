package com.example.rentalSystem.domain.facility.importer.util;

import com.example.rentalSystem.domain.facility.entity.type.FacilityType;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Component
public class FacilityTypeResolver {

    // 자주 나오는 별칭/영문/약어 대응
    private static final Map<String, FacilityType> ALIAS = Map.ofEntries(
            Map.entry("본관", FacilityType.MAIN_BUILDING),
            Map.entry("main", FacilityType.MAIN_BUILDING),
            Map.entry("mainbuilding", FacilityType.MAIN_BUILDING),

            Map.entry("국제관", FacilityType.INTERNATIONAL_BUILDING),
            Map.entry("international", FacilityType.INTERNATIONAL_BUILDING),
            Map.entry("internationalbuilding", FacilityType.INTERNATIONAL_BUILDING),

            Map.entry("학생회관", FacilityType.STUDENT_HALL),
            Map.entry("studenthall", FacilityType.STUDENT_HALL),

            Map.entry("mcc", FacilityType.MCC),

            Map.entry("도서관", FacilityType.LIBRARY),
            Map.entry("library", FacilityType.LIBRARY),

            Map.entry("야외", FacilityType.OUTDOOR),
            Map.entry("outdoor", FacilityType.OUTDOOR)
    );

    /**
     * 느슨 매칭: 공백제거 + 소문자 + 별칭 → 실패시 enum value/name도 스캔
     */
    public Optional<FacilityType> resolveLoose(String raw) {
        if (raw == null) return Optional.empty();

        String key = raw.replaceAll("\\s+", "").toLowerCase(Locale.KOREA);

        // 1) 별칭 우선 매칭
        FacilityType aliasHit = ALIAS.get(key);
        if (aliasHit != null) return Optional.of(aliasHit);

        // 2) enum value(한글 등) 대소문자 무시 매칭
        for (FacilityType t : FacilityType.values()) {
            if (t.getValue().replaceAll("\\s+", "")
                    .equalsIgnoreCase(raw.replaceAll("\\s+", ""))) {
                return Optional.of(t);
            }
        }

        // 3) enum name(MAIN_BUILDING 등)도 허용
        try {
            FacilityType byName = FacilityType.valueOf(raw.trim().toUpperCase(Locale.KOREA));
            return Optional.of(byName);
        } catch (IllegalArgumentException ignore) {
        }

        return Optional.empty();
    }

    /**
     * 엄격 매칭: enum의 한글 value와 정확히 일치해야 함
     */
    public FacilityType resolveStrict(String name) {
        return FacilityType.getInstanceByValue(name.trim());
    }
}