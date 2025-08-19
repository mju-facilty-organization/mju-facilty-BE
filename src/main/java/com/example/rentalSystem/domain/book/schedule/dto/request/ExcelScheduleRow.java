package com.example.rentalSystem.domain.book.schedule.dto.request;

public record ExcelScheduleRow(
    String facilityName,   // 강의실명 (예: S1350)
    String building,       // 건물명 (예: 본관)
    String courseCode,     // 강좌번호 (예: 5974)
    String day,            // 요일 (예: 월/수)
    String startTime,      // 시작시간 (예: 10:00)
    String endTime,        // 종료시간 (예: 11:15)
    String subject,        // 과목명
    String professor,      // 담당교수
    Integer capacity       // 수용인원
) {

}