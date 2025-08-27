package com.example.rentalSystem.domain.facility.dto.response;

import com.example.rentalSystem.domain.book.timetable.TimeStatus;
import com.example.rentalSystem.domain.book.timetable.TimeTable;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.type.FacilityType;
import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
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
    LinkedHashMap<LocalTime, TimeStatus> timeSlot,
    List<ImageMeta> imageMetas
) {

  public static record ImageMeta(String key, String url) {

  }

  public static FacilityDetailResponse of(
      Facility facility,
      TimeTable timeTable,
      List<String> presignedUrls
  ) {
    return FacilityDetailResponse
        .builder()
        .id(facility.getId())
        .facilityType(facility.getFacilityType())
        .facilityNumber(facility.getFacilityNumber())
        .images(presignedUrls)
        .capacity(facility.getCapacity())
        .supportFacilities(facility.getSupportFacilities())
        .date(timeTable.date().toString())
        .timeSlot(timeTable.timeTable())
        .allowedBoundary(facility.getAllowedBoundary())
        .imageMetas(pairKeysWithUrls(facility.getImages(), presignedUrls))
        .build();
  }

  public static FacilityDetailResponse fromFacilityOnly(
      Facility facility,
      List<String> presignedUrls
  ) {
    return FacilityDetailResponse.builder()
        .id(facility.getId())
        .facilityType(facility.getFacilityType())
        .facilityNumber(facility.getFacilityNumber())
        .images(presignedUrls)
        .capacity(facility.getCapacity())
        .allowedBoundary(facility.getAllowedBoundary())
        .supportFacilities(facility.getSupportFacilities())
        .imageMetas(pairKeysWithUrls(facility.getImages(), presignedUrls))
        .build();
  }

  private static List<ImageMeta> pairKeysWithUrls(List<String> keys, List<String> urls) {
    List<ImageMeta> out = new ArrayList<>();
    if (keys == null || urls == null) {
      return out;
    }
    int n = Math.min(keys.size(), urls.size());
    for (int i = 0; i < n; i++) {
      out.add(new ImageMeta(keys.get(i), urls.get(i)));
    }
    return out;
  }
}