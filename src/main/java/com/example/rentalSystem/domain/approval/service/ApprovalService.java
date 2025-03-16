package com.example.rentalSystem.domain.approval.service;

import com.example.rentalSystem.domain.approval.controller.dto.request.RegisterRentalResultRequest;
import com.example.rentalSystem.domain.approval.entity.ProfessorApproval;
import com.example.rentalSystem.domain.approval.implement.ProfessorApprovalImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ApprovalService {

    final ProfessorApprovalImpl professorApprovalImpl;

    public void registerRentalResult(
        Long professorApprovalId,
        RegisterRentalResultRequest registerRentalResultRequest
    ) {
        ProfessorApproval professorApproval = professorApprovalImpl.findById(professorApprovalId);
        professorApproval.registerResult(registerRentalResultRequest);
    }
}
