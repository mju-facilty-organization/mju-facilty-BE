package com.example.rentalSystem.domain.approval.controller.dto.response;

import com.example.rentalSystem.domain.facility.controller.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.rentalhistory.controller.dto.response.RentalInfoResponse;
import com.example.rentalSystem.domain.student.controller.dto.response.StudentInfoResponse;

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
