package com.example.rentalSystem.domain.rentalhistory.controller.dto.response;

import com.example.rentalSystem.domain.approval.entity.ProfessorApproval;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.student.controller.dto.response.StudentInfoResponse;
import com.example.rentalSystem.domain.student.entity.Student;

public record RentalHistoryDetailResponseDto(
    StudentInfoResponse studentInfoResponse,
    RentalHistoryResponseDto rentalHistoryResponseDto
) {

    public static RentalHistoryDetailResponseDto of(RentalHistory rentalHistory,
        ProfessorApproval professorApproval, Student student) {
        return new RentalHistoryDetailResponseDto(
            (StudentInfoResponse.toStudentInfoResponse(student)),
            RentalHistoryResponseDto.toDetailResponseDto(rentalHistory, professorApproval)
        );
    }
}
