package com.example.rentalSystem.domain.rentalhistory.controller.dto.response;

import com.example.rentalSystem.domain.rentalhistory.entity.ProfessorHistory;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalApplicationResult;
import lombok.Builder;

@Builder
public record ProfessorHistoryResponse(
    String professorName,
    String professorAffiliation,
    String professorEmail,
    RentalApplicationResult applicationResult,
    String reason
) {

    public static ProfessorHistoryResponse from(ProfessorHistory professorHistory) {
        return ProfessorHistoryResponse.builder()
            .professorName(professorHistory.getProfessorName())
            .professorAffiliation(professorHistory.getMajor())
            .professorEmail(professorHistory.getProfessorEmail())
            .applicationResult(professorHistory.getRentalApplicationResult())
            .reason(professorHistory.getReason())
            .build();
    }
}
