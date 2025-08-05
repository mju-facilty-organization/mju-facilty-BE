package com.example.rentalSystem.domain.book.approval.controller;

import com.example.rentalSystem.domain.book.approval.dto.request.RegisterRentalResultRequest;
import com.example.rentalSystem.domain.book.approval.dto.response.RentalHistoryResponseForProfessorDto;
import com.example.rentalSystem.domain.book.approval.service.ApprovalService;
import com.example.rentalSystem.global.auth.security.CustomerDetails;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.type.SuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
        @PathVariable(name = "professorApprovalId") Long professorApprovalId,
        @RequestBody RegisterRentalResultRequest registerRentalResultRequest) {
        approvalService.registerRentalResultByProfessor(professorApprovalId,
            registerRentalResultRequest);
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    @Override
    @GetMapping("/professor/{professorApprovalId}")
    public ApiResponse<?> getRentalHistoryInfo(
        @PathVariable(name = "professorApprovalId") Long professorApprovalId
    ) {
        RentalHistoryResponseForProfessorDto rentalHistoryResponseDto = approvalService.getRentalHistoryInfo(
            professorApprovalId);
        return ApiResponse.success(SuccessType.SUCCESS, rentalHistoryResponseDto);
    }

    @Override
    @PostMapping("/pic/{rentalHistoryId}")
    public ApiResponse<?> registerPicApproval(
        @PathVariable Long rentalHistoryId,
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @RequestBody RegisterRentalResultRequest registerRentalResultRequest
    ) {
        approvalService.registerRentalResultByPic(
            rentalHistoryId,
            customerDetails.getPic(),
            registerRentalResultRequest
        );
        return ApiResponse.success(SuccessType.SUCCESS);
    }

}
