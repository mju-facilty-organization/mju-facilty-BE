package com.example.rentalSystem.domain.email.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmailRequest(
    @NotBlank(message = "이메일이 비어있습니다")
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@mju\\.ac\\.kr$",
        message = "명지대 이메일을 사용해야합니다."
    )
    String email
) {

}
