package com.example.rentalSystem.domain.facility.dto.response;

import com.example.rentalSystem.domain.affiliation.type.AffiliationType;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.timeTable.TimeStatus;
import com.example.rentalSystem.domain.facility.entity.timeTable.TimeTable;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.Builder;

@Builder
public record FacilityDetailResponse(
    Long id,
    String facilityType,
    String facilityNumber,
    List<String> images,
    Long capacity,
    List<AffiliationType> allowedBoundary,
    List<String> supportFacilities,
    String pic,
    String date,
    LinkedHashMap<LocalTime, TimeStatus> timeSlot

) {

    public static FacilityDetailResponse of(Facility facility, TimeTable timeTable) {
        return FacilityDetailResponse
            .builder()
            .id(facility.getId())
            .facilityType(facility.getFacilityTypeValue())
            .facilityNumber(facility.getFacilityNumber())
            .images(facility.getImages())
            .capacity(facility.getCapacity())
            .supportFacilities(facility.getSupportFacilities())
            .pic(facility.getPic())
            .date(timeTable.getDate().toString())
            .timeSlot(timeTable.getTimeSlot())
            .allowedBoundary(facility.getAllowedBoundary())
            .build();
    }
}
