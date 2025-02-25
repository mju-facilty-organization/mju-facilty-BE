package com.example.rentalSystem.domain.rentalhistory.controller.dto.response;

import com.example.rentalSystem.domain.affiliation.type.AffiliationType;
import com.example.rentalSystem.domain.rentalhistory.entity.ProfessorHistory;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalApplicationResult;
import lombok.Builder;

@Builder
public record ProfessorHistoryResponse(
    String professorName,
    AffiliationType professorAffiliation,
    String professorEmail,
    RentalApplicationResult applicationResult,
    String reason
) {

    /**
     * Creates a ProfessorHistoryResponse from the provided ProfessorHistory entity.
     *
     * <p>This method maps the professor's rental history details—including name, affiliation,
     * email, rental application result, and reason—from the given entity to a new response object
     * using the builder pattern.</p>
     *
     * @param professorHistory the professor's rental history details
     * @return a new ProfessorHistoryResponse representing the professor's rental history
     */
    public static ProfessorHistoryResponse from(ProfessorHistory professorHistory) {
        return ProfessorHistoryResponse.builder()
            .professorName(professorHistory.getProfessorName())
            .professorAffiliation(professorHistory.getProfessorAffiliation())
            .professorEmail(professorHistory.getProfessorEmail())
            .applicationResult(professorHistory.getRentalApplicationResult())
            .reason(professorHistory.getReason())
            .build();
    }
}
