package com.example.rentalSystem.domain.email.entity;

import lombok.Builder;

@Builder
public record AuthCodeEmail(String title, String authCode, String emailAddress) {

}
