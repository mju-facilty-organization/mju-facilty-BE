package com.example.rentalSystem.global.excel;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class TimeNormalizer {

  private static final DateTimeFormatter HHMM = DateTimeFormatter.ofPattern("HH:mm");
  private static final DateTimeFormatter H_MM = DateTimeFormatter.ofPattern("H:mm", Locale.KOREA);
  private static final DateTimeFormatter H_MMA = DateTimeFormatter.ofPattern("h:mma", Locale.KOREA);
  private static final DateTimeFormatter HA = DateTimeFormatter.ofPattern("ha", Locale.KOREA);


  public LocalTime toLocalTime(Object any) {
    if (any == null) {
      return null;
    }

    if (any instanceof LocalTime lt) {
      return lt;
    }

    if (any instanceof Number num) {
      double v = num.doubleValue();
      double frac = v % 1.0;
      if (frac < 0) {
        frac += 1.0;
      }
      int totalSeconds = (int) Math.round(frac * 24 * 60 * 60);
      int hour = (totalSeconds / 3600) % 24;
      int minute = (totalSeconds % 3600) / 60;
      return LocalTime.of(hour, minute);
    }

    String s = any.toString().trim();
    if (s.isEmpty()) {
      return null;
    }

    String up = s.replace("오전", "AM").replace("오후", "PM").toUpperCase(Locale.KOREA).replaceAll("\\s+", "");

    try {
      if (up.endsWith("AM") || up.endsWith("PM")) {
        return LocalTime.parse(up, H_MMA);
      }
    } catch (Exception ignore) {
    }
    try {
      if (up.endsWith("AM") || up.endsWith("PM")) {
        return LocalTime.parse(up, HA);
      }
    } catch (Exception ignore) {
    }

    if (s.matches("^\\d{1,2}$")) {
      s = s + ":00";
    }
    if (s.matches("^\\d{1,2}:\\d$")) {
      s = s + "0";
    }
    if (s.matches("^\\d:\\d{2}$")) {
      s = "0" + s;
    }

    if (s.matches("^\\d{3,4}$")) {
      int n = Integer.parseInt(s);
      int hour = n / 100;
      int minute = n % 100;
      return LocalTime.of(hour, minute);
    }

    try {
      return LocalTime.parse(s, H_MM);
    } catch (Exception ignore) {
    }
    try {
      return LocalTime.parse(s, HHMM);
    } catch (Exception ignore) {
    }

    throw new ExcelImportException("시간 포맷을 해석할 수 없습니다: " + any);
  }

  public String formatHHmm(LocalTime time) {
    return time == null ? null : time.format(HHMM);
  }
  
  public String formatHColonMm(LocalTime time) {
    return time == null ? null : time.format(H_MM); // "H:mm"
  }
}