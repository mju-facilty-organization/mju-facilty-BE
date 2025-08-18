package com.example.rentalSystem.domain.facility.importer.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class FacilityFieldParsers {
    private FacilityFieldParsers() {
    }

    public static long parseCapacity(String raw) {
        if (raw == null || raw.isBlank()) throw new IllegalArgumentException("수용인원 누락");
        String n = raw.replaceAll("[^0-9]", "");
        if (n.isEmpty()) throw new IllegalArgumentException("수용인원 숫자 아님: " + raw);
        return Long.parseLong(n);
    }

    public static LocalTime parseTime(String raw) {
        if (raw == null || raw.isBlank()) throw new IllegalArgumentException("시간 누락");
        String t = raw.trim()
                .replace("오전", "AM").replace("오후", "PM")
                .replace('.', ':');

        // AM/PM 케이스
        String up = t.toUpperCase(Locale.KOREA).replaceAll("\\s+", "");
        if (up.endsWith("AM") || up.endsWith("PM")) {
            try {
                return LocalTime.parse(up, DateTimeFormatter.ofPattern("h:mma", Locale.KOREA));
            } catch (Exception ignore) {
            }
            return LocalTime.parse(up, DateTimeFormatter.ofPattern("ha", Locale.KOREA));
        }

        // 9 → 9:00, 9:0 → 9:00, 9:30 OK
        if (t.matches("^\\d{1,2}$")) t += ":00";
        if (t.matches("^\\d{1,2}:\\d$")) t += "0";
        if (t.matches("^\\d:\\d{2}$")) t = "0" + t;

        return LocalTime.parse(t, DateTimeFormatter.ofPattern("H:mm", Locale.KOREA));
    }

    public static boolean parseAvailability(String raw) {
        if (raw == null) return true;
        String v = raw.replaceAll("\\s+", "").toLowerCase(Locale.KOREA);
        if (v.contains("불가") || v.contains("사용불가")) return false;
        if (v.contains("가능") || v.contains("사용가능")) return true;
        return v.equals("y") || v.equals("yes") || v.equals("true");
    }

    public static List<String> parseSupportFacilities(String raw) {
        if (raw == null || raw.isBlank()) return List.of();
        return Arrays.stream(raw.split("[,、，/·•‧‥∙·]"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }
}