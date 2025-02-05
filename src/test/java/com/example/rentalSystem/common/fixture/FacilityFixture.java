package com.example.rentalSystem.common.fixture;

import static com.example.rentalSystem.common.fixture.TimeTableFixture.createTimeTable;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.example.rentalSystem.domain.affiliation.type.AffiliationType;
import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.request.UpdateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.response.FacilityDetailResponse;
import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.facility.dto.response.PresignUrlListResponse;
import com.example.rentalSystem.domain.facility.entity.Facility;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class FacilityFixture {

    public static Facility createFacility() {
        Facility facility = Facility.builder()
            .facilityType("본관")
            .facilityNumber("1350")
            .images(List.of("test-file1"))
            .capacity(5L)
            .pic("교수")
            .college(AffiliationType.BUSINESS.getName())
            .isAvailable(true)
            .startTime(LocalTime.now())
            .endTime(LocalTime.now().plusHours(2))
            .supportFacilities(List.of("구비시설1", "구비시설2"))
            .build();
        facility = Mockito.spy(facility);
        doReturn(1L).when(facility).getId();
        doReturn(LocalDateTime.parse("2025-02-02T22:49:40.772231")).when(facility).getCreated_at();
        return facility;
    }

    public static Facility createUpdateFacility() {
//        return Facility.builder()
//            .name("수정된 강의실")
//            .location("수정된 s1353")
//            .capacity(10L)
//            .chargeProfessor("수정된 교수")
//            .isAvailable(true)
//            .startTime(LocalTime.now().minusHours(2))
//            .endTime(LocalTime.now().plusHours(4))
//            .build();
        return null;
    }

    public static CreateFacilityRequestDto createFacilityRequestDto() {

        return new CreateFacilityRequestDto(
            "본관",
            "1350",
            List.of("test-file1"),
            40L,
            List.of("구비시설1", "구비시설2"),
            "책임자",
            LocalTime.now(),
            LocalTime.now(),
            "융합소프트웨어학부",
            true
        );
    }

    public static UpdateFacilityRequestDto createUpdateFacilityRequestDto() {
        return new UpdateFacilityRequestDto(
            "본관",
            "1350",
            "수정된 위치",
            "수정된 교수님",
            new ArrayList<>(List.of("수정 구비시설1", "수정 구비시설2")),
            new ArrayList<>(List.of("목", "금")),
            LocalTime.now().plusHours(2),
            LocalTime.now().plusHours(10),
            10L,
            false
        );
    }

    public static PresignUrlListResponse createFacilityResponseDto() {
        return new PresignUrlListResponse(
            new ArrayList<>(List.of("image1", "image2", "image3"))
        );
    }

    public static FacilityResponse facilityResponse() {
        return FacilityResponse.fromFacility(createFacility());
    }

    public static Page<FacilityResponse> getAllFacilityList(Pageable pageable) {
        List<FacilityResponse> response = List.of(
            facilityResponse());
        Page<FacilityResponse> pageResponse = new PageImpl<>(
            response,
            pageable,
            response.size()
        );
        return pageResponse;
    }

    public static FacilityDetailResponse getFacilityDetail() {
        return FacilityDetailResponse.of(createFacility(), createTimeTable());
    }

}
