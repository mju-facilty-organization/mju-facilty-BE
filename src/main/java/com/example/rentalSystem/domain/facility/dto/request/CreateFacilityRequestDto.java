package com.example.rentalSystem.domain.facility.dto.request;

import com.example.rentalSystem.domain.facility.entity.Facility;
import java.util.List;
import java.util.Map;

public record CreateFacilityRequestDto(
    String name,
    String location,
    List<String> fileNames,
    Long capacity,
    String allowedBoundary,
    List<String> supportFacilities,
    String pic,
    Map<String, List<String>> possibleTimes,
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
            .possibleTimes(possibleTimes)
            .isAvailable(isAvailable)
            .build();
    }
}
