package com.example.rentalSystem.domain.student.controller.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;

public record StudentSignInRequest(
    @Schema(example = "test@mju.ac.kr")
    String email,
    @Schema(example = "test1234")
    String password) {

}
