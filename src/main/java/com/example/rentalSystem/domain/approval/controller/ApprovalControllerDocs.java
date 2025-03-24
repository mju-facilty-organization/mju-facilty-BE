package com.example.rentalSystem.domain.approval.controller;

import com.example.rentalSystem.domain.approval.controller.dto.request.RegisterRentalResultRequest;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.example.ApiErrorCodeExample;
import com.example.rentalSystem.global.response.type.ErrorType;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PathVariable;

public interface ApprovalControllerDocs {

    @Operation(summary = "교수의 대여 승인 API")
    @ApiErrorCodeExample(ErrorType.ENTITY_NOT_FOUND)
    ApiResponse<?> registerProfessorApproval(
        Long rentalHistoryId,
        RegisterRentalResultRequest registerRentalResultRequest
    );

    @Operation(summary = "교수의 대여 승인 페이지 데이터 조회 API")
    ApiResponse<?> registerProfessorApproval(
        @PathVariable(name = "professorApprovalId") Long professorApprovalId
    );
}
