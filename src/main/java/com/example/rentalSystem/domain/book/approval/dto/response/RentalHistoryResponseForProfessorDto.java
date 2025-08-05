package com.example.rentalSystem.domain.book.approval.dto.response;

import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.member.student.dto.response.StudentInfoResponse;
import com.example.rentalSystem.domain.book.rentalhistory.dto.response.RentalInfoResponse;

public record RentalHistoryResponseForProfessorDto(
    Long professorApprovalId,
    StudentInfoResponse studentInfoResponse,
    RentalInfoResponse rentalInfoResponse,
    FacilityResponse facilityResponse

) {

    public static RentalHistoryResponseForProfessorDto of(
        Long professorApprovalId,
        RentalInfoResponse rentalInfoResponse,
        StudentInfoResponse studentInfoResponse,
        FacilityResponse facilityResponse) {
        return new RentalHistoryResponseForProfessorDto(
            professorApprovalId,
            studentInfoResponse,
            rentalInfoResponse,
            facilityResponse
        );
    }
}
