package com.example.rentalSystem.domain.facility.dto.request;

import com.example.rentalSystem.domain.facility.entity.Facility;
import java.time.LocalTime;
import java.util.List;

public record CreateFacilityRequestDto(
    String name,
    String location,
    List<String> fileNames,
    Long capacity,
    String allowedBoundary,
    List<String> supportFacilities,
    String pic,
    LocalTime startTime,
    LocalTime endTime,
    boolean isAvailable
) {

    public Facility toFacility(List<String> imageUrlList) {
        return Facility.builder()
            .name(name)
            .location(location)
            .images(imageUrlList)
            .capacity(capacity)
            .allowedBoundary(allowedBoundary)
            .supportFacilities(supportFacilities)
            .pic(pic)
            .startTime(startTime)
            .endTime(endTime)
            .isAvailable(isAvailable)
            .build();
    }
}
