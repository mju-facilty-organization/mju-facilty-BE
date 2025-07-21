package com.example.rentalSystem.domain.rental.approval.dto.response;

import com.example.rentalSystem.domain.facility.controller.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.member.student.controller.dto.response.StudentInfoResponse;
import com.example.rentalSystem.domain.rental.rentalhistory.dto.response.RentalInfoResponse;

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
