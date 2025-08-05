package com.example.rentalSystem.domain.book.email.controller;


import com.example.rentalSystem.domain.book.email.dto.request.AuthCodeRequest;
import com.example.rentalSystem.domain.book.email.dto.request.EmailRequest;
import com.example.rentalSystem.domain.book.email.dto.response.EmailVerificationResult;
import com.example.rentalSystem.domain.book.email.service.EmailService;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.type.SuccessType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController implements EmailControllerDocs {

    private final EmailService emailService;

    @PostMapping("/check-duplicate")
    public ApiResponse<?> checkDuplicatedEmail(@Valid @RequestBody EmailRequest emailRequest) {
        emailService.checkDuplicatedEmail(emailRequest);
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    @PostMapping("/send-code")
    public ApiResponse<?> sendCodeToEmail(@Valid @RequestBody EmailRequest emailRequest) {
        emailService.sendCodeToEmail(emailRequest);
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    @PostMapping("/check-code")
    public ApiResponse<EmailVerificationResult> checkCode(
        @RequestBody AuthCodeRequest authCodeRequest
    ) {
        EmailVerificationResult emailVerificationResult = emailService.verificationCode(
            authCodeRequest.email(), authCodeRequest.authCode());
        return ApiResponse.success(SuccessType.SUCCESS, emailVerificationResult);
    }
}
