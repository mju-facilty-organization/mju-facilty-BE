package com.example.rentalSystem.domain.email.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthCodeRequest(
    @Schema(description = "사용자 이메일", example = "test@mju.ac.kr")
    String email,
    @Schema(description = "인증코드", example = "123456")
    String authCode) {

}
