package com.example.rentalSystem.domain.rental.approval.controller;

import com.example.rentalSystem.domain.rental.approval.dto.request.RegisterRentalResultRequest;
import com.example.rentalSystem.global.auth.security.CustomerDetails;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.example.ApiErrorCodeExample;
import com.example.rentalSystem.global.response.type.ErrorType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "대여 내역에 대한 \"승인\" 관련 API")
public interface ApprovalControllerDocs {

    @Operation(summary = "교수의 대여 승인 API")
    @ApiErrorCodeExample(ErrorType.ENTITY_NOT_FOUND)
    ApiResponse<?> registerProfessorApproval(
        Long professorApprovalId,
        RegisterRentalResultRequest registerRentalResultRequest
    );

    @Operation(summary = "교수의 대여 승인 페이지 데이터 조회 API")
    ApiResponse<?> getRentalHistoryInfo(
        @PathVariable(name = "professorApprovalId") Long professorApprovalId
    );

    @Operation(summary = "교직원 대여 승인 API")
    ApiResponse<?> registerPicApproval(
        Long rentalHistoryId,
        CustomerDetails customerDetails,
        RegisterRentalResultRequest registerRentalResultRequest
    );

}
