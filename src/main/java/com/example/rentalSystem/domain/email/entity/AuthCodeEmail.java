package com.example.rentalSystem.domain.email.entity;

import lombok.Builder;

@Builder
public record AuthCodeEmail(String title, String authCode, String emailAddress) {

    public static final String INTRODUCE = "명지대 Rental 가입 인증 코드입니다.\n";
}
