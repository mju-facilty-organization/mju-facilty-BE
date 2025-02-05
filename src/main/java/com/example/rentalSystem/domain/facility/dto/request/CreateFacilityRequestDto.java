package com.example.rentalSystem.domain.facility.dto.request;

import com.example.rentalSystem.domain.facility.entity.Facility;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;

public record CreateFacilityRequestDto(
    @NotEmpty
    String facilityType,
    @NotEmpty
    String facilityNumber,
    List<String> fileNames,
    @NotNull
    Long capacity,
    List<String> supportFacilities,
    @NotEmpty
    String pic,
    LocalTime startTime,
    LocalTime endTime,
    @NotEmpty
    String college,
    boolean isAvailable
) {

    public Facility toFacility(List<String> imageUrlList) {
        return Facility.builder()
            .facilityType(facilityType)
            .facilityNumber(facilityNumber)
            .images(imageUrlList)
            .capacity(capacity)
            .supportFacilities(supportFacilities)
            .pic(pic)
            .startTime(startTime)
            .endTime(endTime)
            .isAvailable(isAvailable)
            .college(college)
            .build();
    }
}

