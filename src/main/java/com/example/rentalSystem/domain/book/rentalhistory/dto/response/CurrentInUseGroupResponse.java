package com.example.rentalSystem.domain.book.rentalhistory.dto.response;

import com.example.rentalSystem.domain.book.rentalhistory.entity.RentalHistory;

import java.time.LocalDateTime;

public record CurrentInUseGroupResponse(
        Long rentalHistoryId,
        String organization,
        String purpose,
        int numberOfPeople,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static CurrentInUseGroupResponse from(RentalHistory r) {
        return new CurrentInUseGroupResponse(
                r.getId(),
                r.getOrganization(),
                r.getPurpose(),
                r.getNumberOfPeople(),
                r.getStartDateTime(),
                r.getEndDateTime()
        );
    }
}