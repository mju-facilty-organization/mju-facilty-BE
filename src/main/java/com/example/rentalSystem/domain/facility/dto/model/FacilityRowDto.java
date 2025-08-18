package com.example.rentalSystem.domain.facility.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * XLS 한 행 DTO (헤더 고정)
 * 시설유형 | 시설번호 | 수용인원 | 시작시간 | 종료시간 | 단과대학 | 이용범위 | 지원시설 | 사용가능여부
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityRowDto {
    private String facilityTypeKo;       // 시설유형 (본관, 국제관, ...)
    private String facilityNumber;       // 시설번호 (예: 1350)
    private String capacityKo;           // 수용인원 (문자열 → 숫자 파싱)
    private String startTime;            // 시작시간 (예: 9:00, 09:00, 오전 9:00)
    private String endTime;              // 종료시간
    private String collegeKo;            // 단과대학 (예: ICT 융합대학)
    private String allowedRangeKo;       // 이용범위 (전공/학과 콤마 구분) - 없으면 null/빈 문자열
    private String supportFacilitiesKo;  // 지원시설 (콤마 구분) - 없을 수 있음
    private String availableKo;          // 사용가능여부 (사용가능/사용 불가/Y/N/true/false) - 없으면 true 기본
}