package com.example.rentalSystem.domain.rental.rentalhistory.dto.response;

import com.example.rentalSystem.domain.rental.approval.entity.ProfessorApproval;
import com.example.rentalSystem.domain.member.pic.dto.response.PicInfoResponse;
import com.example.rentalSystem.domain.member.pic.entity.Pic;
import com.example.rentalSystem.domain.member.student.dto.response.StudentInfoResponse;
import com.example.rentalSystem.domain.member.student.entity.Student;
import com.example.rentalSystem.domain.rental.rentalhistory.entity.RentalHistory;
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
