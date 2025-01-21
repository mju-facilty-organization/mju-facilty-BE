package com.example.rentalSystem.common.fixture;

import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.request.UpdateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.entity.Facility;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FacilityFixture {

    public static Facility createFacility() {
//        return Facility.builder()
//            .name("강의실")
//            .location("s1353")
//            .capacity(5L)
//            .chargeProfessor("교수")
//            .isAvailable(true)
//            .startTime(LocalTime.now())
//            .endTime(LocalTime.now().plusHours(2))
//            .build();
        return null;
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
//    return new CreateFacilityRequestDto(
//        "이름",
//        5L,
//        "위치",
//        "교수님",
//        new ArrayList<>(List.of("구비시설1", "구비시설2")),
//        new ArrayList<>(List.of("월", "화")),
//        LocalTime.now(),
//        LocalTime.now(),
//        true
//        );
        return null;
    }

    public static UpdateFacilityRequestDto createUpdateFacilityRequestDto() {
        return new UpdateFacilityRequestDto(
            "수정된 이름",
            10L,
            "수정된 위치",
            "수정된 교수님",
            new ArrayList<>(List.of("수정 구비시설1", "수정 구비시설2")),
            new ArrayList<>(List.of("목", "금")),
            LocalTime.now().plusHours(2),
            LocalTime.now().plusHours(10),
            false
        );
    }
}
