package com.example.rentalSystem.domain.email.presentation.controller;


import com.example.rentalSystem.domain.email.dto.request.AuthCodeRequest;
import com.example.rentalSystem.domain.email.dto.request.EmailRequest;
import com.example.rentalSystem.domain.email.dto.response.EmailVerificationResult;
import com.example.rentalSystem.domain.email.service.EmailService;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.SuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/check-email")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> checkDuplicatedEmail(@RequestBody EmailRequest emailRequest) {
        emailService.checkDuplicatedEmail(emailRequest.email());
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    @PostMapping("/send-code")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> sendCodeToEmail(@RequestBody EmailRequest emailRequest) {
        emailService.sendCodeToEmail(emailRequest.email());
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    @GetMapping("/sign-up/emails/check-code")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> checkCode(@RequestBody AuthCodeRequest authCodeRequest) {
        EmailVerificationResult emailVerificationResult = emailService.verificationCode(
            authCodeRequest.email(), authCodeRequest.authCode());
        return ApiResponse.success(SuccessType.SUCCESS, emailVerificationResult);
    }

}
