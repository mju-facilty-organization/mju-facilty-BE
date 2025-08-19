package com.example.rentalSystem.domain.facility.importer.util;

public final class FacilityNumberNormalizer {
    private FacilityNumberNormalizer() {
    }

    /**
     * 규칙:
     * - trim + 대문자화
     * - 이미 'S'+숫자면 유지 (예: S1350)
     * - 숫자만 있으면 'S' 접두 부여 (예: 1350 → S1350)
     * - 그 외 문자는 제거하고 숫자만 추출해 'S'+숫자 (숫자 없으면 원본 반환)
     */
    public static String normalize(String raw) {
        if (raw == null) return null;
        String s = raw.trim().toUpperCase().replaceAll("\\s+", "");

        if (s.matches("^S\\d+$")) return s;       // 정상
        if (s.matches("^\\d+$")) return "S" + s;  // 숫자만

        if (s.startsWith("S")) {
            String digits = s.substring(1).replaceAll("\\D", "");
            return digits.isEmpty() ? "S" : "S" + digits;
        }
        String digits = s.replaceAll("\\D", "");
        return digits.isEmpty() ? s : "S" + digits;
    }
}