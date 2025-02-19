package com.example.rentalSystem.domain.rentalhistory.controller.dto.response;

import com.example.rentalSystem.domain.rentalhistory.entity.ProfessorHistory;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.student.controller.dto.response.StudentResponse;
import com.example.rentalSystem.domain.student.entity.Student;

public record RentalHistoryDetailResponseDto(
    StudentResponse studentResponse,
    RentalHistoryResponseDto rentalHistoryResponseDto
) {

    public static RentalHistoryDetailResponseDto of(RentalHistory rentalHistory,
        ProfessorHistory professorHistory, Student student) {
        return new RentalHistoryDetailResponseDto(
            (StudentResponse.toStudentResponse(student)),
            RentalHistoryResponseDto.toDetailResponseDto(rentalHistory, professorHistory)
        );
    }
}
