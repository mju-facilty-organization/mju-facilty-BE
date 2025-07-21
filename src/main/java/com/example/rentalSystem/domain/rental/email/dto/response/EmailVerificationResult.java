package com.example.rentalSystem.domain.rental.email.dto.response;

public record EmailVerificationResult(boolean authResult) {

    public static EmailVerificationResult from(boolean authResult) {
        return new EmailVerificationResult(authResult);
    }
}
