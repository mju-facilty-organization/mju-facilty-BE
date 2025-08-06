package com.example.rentalSystem.domain.email.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmailRequest(
    @NotBlank(message = "이메일이 비어있습니다")
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@mju\\.ac\\.kr$",
        message = "명지대 이메일을 사용해야합니다."
    )
    @Schema(description = "사용자 이메일", example = "test@mju.ac.kr")
    String email
) {

}
