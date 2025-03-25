package com.example.rentalSystem.domain.rentalhistory.controller.dto.response;

import com.example.rentalSystem.domain.approval.entity.ProfessorApproval;
import com.example.rentalSystem.domain.pic.controller.dto.response.PicInfoResponse;
import com.example.rentalSystem.domain.pic.entity.Pic;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.student.controller.dto.response.StudentInfoResponse;
import com.example.rentalSystem.domain.student.entity.Student;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;

@JsonInclude(Include.NON_NULL)
@Builder
public record RentalHistoryDetailResponseDto(
    StudentInfoResponse studentInfoResponse,
    RentalHistoryResponseDto rentalHistoryResponseDto,
    PicInfoResponse picInfoResponse
) {

    public static RentalHistoryDetailResponseDto of(RentalHistory rentalHistory,
        ProfessorApproval professorApproval, Student student) {
        return RentalHistoryDetailResponseDto.builder()
            .studentInfoResponse(StudentInfoResponse.toStudentInfoResponse(student))
            .rentalHistoryResponseDto(
                RentalHistoryResponseDto.toDetailResponseDto(rentalHistory, professorApproval))
            .build();
    }

    public static RentalHistoryDetailResponseDto of(
        RentalHistory rentalHistory,
        ProfessorApproval professorApproval,
        Student student,
        Pic pic) {
        return RentalHistoryDetailResponseDto.builder()
            .studentInfoResponse(StudentInfoResponse.toStudentInfoResponse(student))
            .rentalHistoryResponseDto(
                RentalHistoryResponseDto.toDetailResponseDto(rentalHistory, professorApproval))
            .picInfoResponse(PicInfoResponse.from(pic))
            .build();
    }
}
