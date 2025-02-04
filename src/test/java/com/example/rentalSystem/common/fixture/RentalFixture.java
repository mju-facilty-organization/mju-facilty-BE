package com.example.rentalSystem.common.fixture;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.rentalhistory.dto.request.CreateRentalRequest;
import com.example.rentalSystem.domain.rentalhistory.dto.response.RentalHistoryResponseDto;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.student.entity.Student;
import java.time.LocalDateTime;
import java.util.List;

public class RentalFixture {

    public static CreateRentalRequest createRentalRequest() {
        return new CreateRentalRequest(
            "1",
            LocalDateTime.parse("2025-01-31T12:00:00"),
            LocalDateTime.parse("2025-01-31T16:00:00"),
            "COW",
            "10",
            "최성운",
            "동아리 활동"
        );
    }

    public static RentalHistory createRentalHistory() {
        Facility facility = FacilityFixture.createFacility();
        Student student = StudentFixture.createStudent();
        CreateRentalRequest createRentalRequest = createRentalRequest();
        return createRentalRequest.toEntity(student, facility);
    }

    public static RentalHistoryResponseDto createRentalHistoryResponseDto() {
        return RentalHistoryResponseDto.from(createRentalHistory());
    }

    public static List<RentalHistoryResponseDto> createAllRentalHistory() {
        return List.of(createRentalHistoryResponseDto());
    }
}
