package com.example.rentalSystem.domain.approval.controller;

import com.example.rentalSystem.domain.professor.controller.dto.request.RegisterRentalResultRequest;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.example.ApiErrorCodeExample;
import com.example.rentalSystem.global.response.type.ErrorType;
import io.swagger.v3.oas.annotations.Operation;

public interface ApprovalControllerDocs {

    @Operation(summary = "교수의 대여 승인 API")
    @ApiErrorCodeExample(ErrorType.ENTITY_NOT_FOUND)
    ApiResponse<?> registerProfessorApproval(
        Long rentalHistoryId,
        RegisterRentalResultRequest registerRentalResultRequest);
}
