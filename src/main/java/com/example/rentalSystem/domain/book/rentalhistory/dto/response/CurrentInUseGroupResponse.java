package com.example.rentalSystem.domain.book.rentalhistory.dto.response;

import com.example.rentalSystem.domain.book.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.member.student.dto.response.StudentInfoResponse;

import java.time.LocalDateTime;

public record CurrentInUseGroupResponse(
        Long rentalHistoryId,
        String organization,
        String purpose,
        int numberOfPeople,
        LocalDateTime startTime,
        LocalDateTime endTime,
        StudentInfoResponse applicant
) {
    public static CurrentInUseGroupResponse from(RentalHistory r) {
        return new CurrentInUseGroupResponse(
                r.getId(),
                r.getOrganization(),
                r.getPurpose(),
                r.getNumberOfPeople(),
                r.getStartDateTime(),
                r.getEndDateTime(),
                StudentInfoResponse.toStudentInfoResponse(r.getStudent())
        );
    }
}