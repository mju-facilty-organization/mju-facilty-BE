package com.example.rentalSystem.domain.email.controller;

import com.example.rentalSystem.domain.email.dto.request.AuthCodeRequest;
import com.example.rentalSystem.domain.email.dto.request.EmailRequest;
import com.example.rentalSystem.domain.email.dto.response.EmailVerificationResult;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.example.ApiErrorCodeExample;
import com.example.rentalSystem.global.response.example.ApiErrorCodeExamples;
import com.example.rentalSystem.global.response.type.ErrorType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "이메일", description = "이메일 인증, 전송 API")
public interface EmailControllerDocs {

    @Operation(summary = "이메일 중복 확인", description = "이메일 중복시 예외 발생")
    @ApiErrorCodeExample(ErrorType.DUPLICATE_EMAIL_RESOURCE)
    ApiResponse<?> checkDuplicatedEmail(EmailRequest emailRequest);

    @Operation(summary = "인증코드 발송", description = "이메일에 6자리 인증 코드 발송")
    @ApiErrorCodeExamples(
        {ErrorType.BAD_REQUEST, ErrorType.DUPLICATE_SEND, ErrorType.FAIL_SEND_EMAIL}
    )
    ApiResponse<?> sendCodeToEmail(EmailRequest emailRequest);

    @Operation(summary = "인증코드 검증", description = "인증 코드 6자리가 맞는 지 확인")
    ApiResponse<EmailVerificationResult> checkCode(AuthCodeRequest authCodeRequest);
}
