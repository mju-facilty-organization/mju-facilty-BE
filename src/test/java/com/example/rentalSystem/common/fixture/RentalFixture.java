package com.example.rentalSystem.common.fixture;

import static org.mockito.Mockito.doReturn;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.member.student.entity.Student;
import com.example.rentalSystem.domain.rental.rentalhistory.dto.request.CreateRentalRequest;
import com.example.rentalSystem.domain.rental.rentalhistory.dto.response.RentalHistoryResponseDto;
import com.example.rentalSystem.domain.rental.rentalhistory.entity.RentalHistory;
import java.time.LocalDateTime;
import java.util.List;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class RentalFixture {

    public static CreateRentalRequest createRentalRequest() {
        return new CreateRentalRequest(
            "1",
            LocalDateTime.parse("2025-01-31T12:00:00"),
            LocalDateTime.parse("2025-01-31T16:00:00"),
            "COW",
            10,
            "최성운",
            "동아리 활동"
        );
    }

    public static RentalHistory createRentalHistory() {
        Facility facility = FacilityFixture.createFacility();
        Student student = StudentFixture.createStudent();
        CreateRentalRequest createRentalRequest = createRentalRequest();
        RentalHistory rentalHistory = createRentalRequest.toEntity(student, facility);
        rentalHistory = Mockito.spy(rentalHistory);
        doReturn(1L).when(rentalHistory).getId();
        doReturn(LocalDateTime.parse("2025-02-02T22:49:40.772231")).when(rentalHistory)
            .getCreated_at();

        return rentalHistory;
    }

    public static RentalHistoryResponseDto createRentalHistoryResponseDto() {
        return RentalHistoryResponseDto.from(createRentalHistory());
    }

    public static Page<RentalHistoryResponseDto> createAllRentalHistory(Pageable pageable) {
        List<RentalHistoryResponseDto> rentalHistoryResponseDtos = List.of(
            createRentalHistoryResponseDto());
        Page<RentalHistoryResponseDto> rentalHistoryPage = new PageImpl<>(
            rentalHistoryResponseDtos, pageable,
            rentalHistoryResponseDtos.size());

        return rentalHistoryPage;
    }
}
