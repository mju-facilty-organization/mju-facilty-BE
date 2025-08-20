package com.example.rentalSystem.domain.book.schedule.importer.util;

import java.time.DayOfWeek;

public final class DayOfWeekResolver {

  private DayOfWeekResolver() {
  }

  public static DayOfWeek resolve(String value) {
    if (value == null) {
      throw new IllegalArgumentException("요일이 비어 있습니다.");
    }
    return switch (value.trim()) {
      case "월", "MON", "MONDAY" -> DayOfWeek.MONDAY;
      case "화", "TUE", "TUESDAY" -> DayOfWeek.TUESDAY;
      case "수", "WED", "WEDNESDAY" -> DayOfWeek.WEDNESDAY;
      case "목", "THU", "THURSDAY" -> DayOfWeek.THURSDAY;
      case "금", "FRI", "FRIDAY" -> DayOfWeek.FRIDAY;
      case "토", "SAT", "SATURDAY" -> DayOfWeek.SATURDAY;
      case "일", "SUN", "SUNDAY" -> DayOfWeek.SUNDAY;
      default -> throw new IllegalArgumentException("지원하지 않는 요일: " + value);
    };
  }
}