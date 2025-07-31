package com.example.rentalSystem.domain.facility.dto.response;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.timeTable.TimeStatus;
import com.example.rentalSystem.domain.facility.entity.timeTable.TimeTable;
import com.example.rentalSystem.domain.facility.entity.type.FacilityType;
import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.Builder;

@Builder
public record FacilityDetailResponse(
    Long id,
    FacilityType facilityType,
    String facilityNumber,
    List<String> images,
    Long capacity,
    List<AffiliationType> allowedBoundary,
    List<String> supportFacilities,
    String pic,
    String date,
    LinkedHashMap<LocalTime, TimeStatus> timeSlot

) {

    public static FacilityDetailResponse of(Facility facility, TimeTable timeTable,
        List<String> presignedUrls) {
        return FacilityDetailResponse
            .builder()
            .id(facility.getId())
            .facilityType(facility.getFacilityType())
            .facilityNumber(facility.getFacilityNumber())
            .images(presignedUrls)
            .capacity(facility.getCapacity())
            .supportFacilities(facility.getSupportFacilities())
            .date(timeTable.getDate().toString())
            .timeSlot(timeTable.getTimeSlot())
            .allowedBoundary(facility.getAllowedBoundary())
            .build();
    }
}
