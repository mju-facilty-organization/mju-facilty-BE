package com.example.rentalSystem.domain.facility.importer.mapper;

import static com.example.rentalSystem.domain.facility.importer.util.FacilityNumberNormalizer.normalize;

import com.example.rentalSystem.domain.facility.dto.model.FacilityRowDto;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.type.FacilityType;
import com.example.rentalSystem.domain.facility.importer.util.AffiliationLookup;
import com.example.rentalSystem.domain.facility.importer.util.FacilityFieldParsers;
import com.example.rentalSystem.domain.facility.importer.util.FacilityTypeResolver;
import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FacilityRowMapper {

  private final FacilityTypeResolver typeResolver;
  private final AffiliationLookup affiliationLookup;

  public Facility toNewEntity(FacilityRowDto row) {
    FacilityType type = typeResolver.resolveLoose(row.getFacilityTypeKo())
        .orElseThrow(() -> new IllegalArgumentException("시설유형 매핑 실패: " + row.getFacilityTypeKo()));

    long capacity = FacilityFieldParsers.parseCapacity(row.getCapacityKo());
    LocalTime start = FacilityFieldParsers.parseTime(row.getStartTime());
    LocalTime end = FacilityFieldParsers.parseTime(row.getEndTime());
    if (!start.isBefore(end)) {
      throw new IllegalArgumentException("시작시간<종료시간 위반: " + row.getStartTime() + "~" + row.getEndTime());
    }

    List<AffiliationType> boundary = affiliationLookup.resolveMajorsOrDefault(row.getAllowedRangeKo(),
        row.getCollegeKo());
    if (boundary.isEmpty()) {
      throw new IllegalArgumentException("이용범위 결과 비어있음");
    }

    List<String> supports = FacilityFieldParsers.parseSupportFacilities(row.getSupportFacilitiesKo());
    boolean available = FacilityFieldParsers.parseAvailability(row.getAvailableKo());

    return Facility.builder()
        .facilityType(type.getValue())
        .facilityNumber(normalize(row.getFacilityNumber())) // ★ 정규화
        .images(List.of())
        .capacity(capacity)
        .supportFacilities(supports)
        .startTime(start)
        .endTime(end)
        .allowedBoundary(boundary)
        .isAvailable(available)
        .build();
  }

  public void applyToExisting(Facility target, FacilityRowDto row) {
    // 1) 수용인원 파싱
    long capacity = FacilityFieldParsers.parseCapacity(row.getCapacityKo());

    // 2) 시작/종료 시간 파싱 + 검증
    LocalTime start = FacilityFieldParsers.parseTime(row.getStartTime());
    LocalTime end = FacilityFieldParsers.parseTime(row.getEndTime());
    if (!start.isBefore(end)) {
      throw new IllegalArgumentException("시작시간<종료시간 위반: " + row.getStartTime() + "~" + row.getEndTime());
    }

    // 3) 단과대학/이용범위 파싱
    List<AffiliationType> boundary =
        affiliationLookup.resolveMajorsOrDefault(row.getAllowedRangeKo(), row.getCollegeKo());
    if (boundary.isEmpty()) {
      throw new IllegalArgumentException("이용범위 결과 비어있음");
    }

    // 4) 지원시설/가용여부 파싱
    List<String> supports = FacilityFieldParsers.parseSupportFacilities(row.getSupportFacilitiesKo());
    boolean available = FacilityFieldParsers.parseAvailability(row.getAvailableKo());

    // 5) 타입 파싱 (시설번호는 변경 안 함 → 매핑 키니까)
    FacilityType newType = typeResolver.resolveLoose(row.getFacilityTypeKo())
        .orElseThrow(() -> new IllegalArgumentException("시설유형 매핑 실패: " + row.getFacilityTypeKo()));

    // 6) 실제 업데이트
    target.updateAll(
        newType,                       // 타입은 업데이트
        null,                          // 시설번호는 매핑 키 → 바꾸지 않음
        capacity,
        start,
        end,
        supports,
        boundary,
        available
    );
  }
}