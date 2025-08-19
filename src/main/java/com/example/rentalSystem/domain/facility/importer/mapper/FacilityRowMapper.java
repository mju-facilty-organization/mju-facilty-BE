package com.example.rentalSystem.domain.facility.importer.mapper;

import com.example.rentalSystem.domain.facility.dto.model.FacilityRowDto;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.type.FacilityType;
import com.example.rentalSystem.domain.facility.importer.util.AffiliationLookup;
import com.example.rentalSystem.domain.facility.importer.util.FacilityFieldParsers;
import com.example.rentalSystem.domain.facility.importer.util.FacilityTypeResolver;
import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

import static com.example.rentalSystem.domain.facility.importer.util.FacilityNumberNormalizer.normalize;

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

        List<AffiliationType> boundary = affiliationLookup.resolveMajorsOrDefault(row.getAllowedRangeKo(), row.getCollegeKo());
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
        long capacity = FacilityFieldParsers.parseCapacity(row.getCapacityKo());
        LocalTime start = FacilityFieldParsers.parseTime(row.getStartTime());
        LocalTime end = FacilityFieldParsers.parseTime(row.getEndTime());
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("시작시간<종료시간 위반: " + row.getStartTime() + "~" + row.getEndTime());
        }

        List<AffiliationType> boundary = affiliationLookup.resolveMajorsOrDefault(row.getAllowedRangeKo(), row.getCollegeKo());
        if (boundary.isEmpty()) {
            throw new IllegalArgumentException("이용범위 결과 비어있음");
        }

        List<String> supports = FacilityFieldParsers.parseSupportFacilities(row.getSupportFacilitiesKo());
        boolean available = FacilityFieldParsers.parseAvailability(row.getAvailableKo());

        // 번호 변경 정책이 필요하면 아래 주석 해제 (보통 키는 바꾸지 않음)
        // target.setFacilityNumber(normalize(row.getFacilityNumber()));

//        target.updateAll(capacity, start, end, supports, boundary, available);
    }
}