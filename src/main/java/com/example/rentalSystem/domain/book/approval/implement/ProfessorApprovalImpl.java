package com.example.rentalSystem.domain.book.approval.implement;

import com.example.rentalSystem.domain.book.approval.entity.ProfessorApproval;
import com.example.rentalSystem.domain.book.approval.repository.ProfessorApprovalRepository;
import com.example.rentalSystem.domain.book.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfessorApprovalImpl {

    final ProfessorApprovalRepository professorApprovalRepository;

    public ProfessorApproval save(ProfessorApproval professorApproval) {
        return professorApprovalRepository.save(professorApproval);
    }

    public ProfessorApproval findByRentalHistory(RentalHistory rentalHistory) {
        return professorApprovalRepository.findByRentalHistory(rentalHistory)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }

    public ProfessorApproval findById(Long professorRentalHistoryId) {
        return professorApprovalRepository.findById(professorRentalHistoryId)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }
}
