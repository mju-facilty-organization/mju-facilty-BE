package com.example.rentalSystem.domain.facility.importer.util;

import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * allowedRangeKo(전공/학과 목록) → AffiliationType
 * - allowedRangeKo가 비었으면 collegeKo(단과대학)의 하위 전공 전체로 채움
 * - allowedRangeKo가 있으면 개별 전공/학과만 매핑
 */
@Component
public class AffiliationLookup {

    public List<AffiliationType> resolveMajorsOrDefault(String allowedRangeKo, String collegeKo) {
        List<AffiliationType> out = new ArrayList<>();

        if (allowedRangeKo != null && !allowedRangeKo.isBlank()) {
            String[] parts = allowedRangeKo.split("[,、，/·•‧‥∙·]");
            for (String p : parts) {
                String major = p.trim();
                if (major.isEmpty()) continue;
                // 전공/학과 이름이 enum의 name 필드(한글명)와 정확히 일치해야 함
                out.add(AffiliationType.getInstance(major));
            }
            return out;
        }

        // 기본: 단과대학 전체 허용
        if (collegeKo == null || collegeKo.isBlank()) {
            return out; // 빈 리스트 (검증/매퍼에서 에러 처리)
        }
        return AffiliationType.getChildList(collegeKo.trim());
    }
}

//package com.example.rentalSystem.domain.facility.importer.util;
//
//import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
//import org.springframework.stereotype.Component;
//
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Optional;
//
/// **
// * 한글 전공/학과/단과대 문자열 → AffiliationType 매핑 유틸
// */
//@Component
//public class AffiliationLookup {
//
//    private static String norm(String s) {
//        return s == null ? "" : s.replaceAll("\\s+", "").toLowerCase();
//    }
//
//    /**
//     * 단일 항목을 느슨 매칭
//     */
//    public Optional<AffiliationType> tryResolve(String raw) {
//        if (raw == null) return Optional.empty();
//        String key = norm(raw);
//        for (AffiliationType t : AffiliationType.values()) {
//            String tKey = norm(t.getName());
//            if (tKey.equals(key)) return Optional.of(t);
//        }
//        return Optional.empty();
//    }
//
//    /**
//     * "응용소프트웨어전공, 디지털콘텐츠디자인학과" → [SOFTWARE_APPLICATIONS, DIGITAL_CONTENTS_DESIGN]
//     */
//    public List<AffiliationType> majorsFromCsv(String csv) {
//        if (csv == null || csv.isBlank()) return List.of();
//        LinkedHashSet<AffiliationType> set = new LinkedHashSet<>();
//        for (String token : csv.split("[,、，/·•‧‥∙·]")) {
//            String p = token.trim();
//            tryResolve(p).ifPresent(t -> {
//                // 전공/학과만 허용 (parent가 있는 항목)
//                if (t.getParent() != null) set.add(t);
//            });
//        }
//        return List.copyOf(set);
//    }
//
//    /**
//     * 이용범위가 비었으면 단과대 하위 전공 전체로 대체
//     * - 이용범위가 있으면 전공/학과만 허용
//     */
//    public List<AffiliationType> resolveMajorsOrDefault(String rangeCsv, String collegeKo) {
//        List<AffiliationType> majors = majorsFromCsv(rangeCsv);
//        if (!majors.isEmpty()) return majors;
//
//        // 단과대 매핑
//        AffiliationType college = tryResolve(collegeKo)
//                .orElseThrow(() -> new IllegalArgumentException("단과대학 매핑 실패: " + collegeKo));
//        if (college.getParent() != null) {
//            // 전공/학과가 들어왔으면 오류
//            throw new IllegalArgumentException("단과대학 칸에 전공/학과가 들어왔습니다: " + collegeKo);
//        }
//        return AffiliationType.getChildList(college.getName());
//    }
//}