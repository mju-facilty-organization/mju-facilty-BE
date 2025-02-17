package com.example.rentalSystem.domain.email.controller.dto.response;

public record EmailVerificationResult(boolean authResult) {

    public static EmailVerificationResult of(boolean authResult) {
        return new EmailVerificationResult(authResult);
    }
}
