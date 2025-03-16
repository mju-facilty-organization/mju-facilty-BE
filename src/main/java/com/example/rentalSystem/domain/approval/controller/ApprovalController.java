package com.example.rentalSystem.domain.approval.controller;

import com.example.rentalSystem.domain.approval.controller.dto.request.RegisterRentalResultRequest;
import com.example.rentalSystem.domain.approval.service.ApprovalService;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.type.SuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/approval")
@RequiredArgsConstructor
public class ApprovalController implements ApprovalControllerDocs {

    final ApprovalService approvalService;

    @Override
    @PostMapping("/professor/{professorApprovalId}")
    public ApiResponse<?> registerProfessorApproval(
        @PathVariable Long professorApprovalId,
        @RequestBody RegisterRentalResultRequest registerRentalResultRequest) {
        approvalService.registerRentalResult(professorApprovalId, registerRentalResultRequest);
        return ApiResponse.success(SuccessType.SUCCESS);
    }
}
