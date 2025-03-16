package com.example.rentalSystem.domain.approval.controller.dto.response;

import com.example.rentalSystem.domain.affiliation.type.AffiliationType;
import com.example.rentalSystem.domain.approval.entity.ProfessorApproval;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalApplicationResult;
import lombok.Builder;

@Builder
public record ProfessorApprovalResponse(
    String professorName,
    AffiliationType professorAffiliation,
    String professorEmail,
    RentalApplicationResult applicationResult,
    String reason
) {

    public static ProfessorApprovalResponse from(ProfessorApproval professorApproval) {
        return ProfessorApprovalResponse.builder()
            .professorName(professorApproval.getProfessorName())
            .professorAffiliation(professorApproval.getProfessorAffiliation())
            .professorEmail(professorApproval.getProfessorEmail())
            .applicationResult(professorApproval.getRentalApplicationResult())
            .reason(professorApproval.getReason())
            .build();
    }
}
