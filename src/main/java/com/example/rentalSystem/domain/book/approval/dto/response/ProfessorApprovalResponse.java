package com.example.rentalSystem.domain.book.approval.dto.response;

import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import com.example.rentalSystem.domain.book.approval.entity.ProfessorApproval;
import com.example.rentalSystem.domain.book.rentalhistory.entity.type.RentalApplicationResult;
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
