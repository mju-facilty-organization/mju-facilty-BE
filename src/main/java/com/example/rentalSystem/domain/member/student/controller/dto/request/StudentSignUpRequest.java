package com.example.rentalSystem.domain.member.student.controller.dto.request;

import com.example.rentalSystem.domain.member.student.entity.Student;
import io.swagger.v3.oas.annotations.media.Schema;

public record StudentSignUpRequest(
    @Schema(example = "아무개")
    String name,
    @Schema(example = "60200000")
    String studentNumber,
    @Schema(example = "test1234")
    String password,
    @Schema(example = "test@mju.ac.kr")
    String email,
    @Schema(example = "응용소프트웨어전공")
    String major,
    @Schema(example = "010-0000-0000")
    String phoneNumber) {

    public Student toEntity(String encodePassword) {
        return Student.builder()
            .name(name)
            .password(encodePassword)
            .phoneNumber(phoneNumber)
            .studentNumber(studentNumber)
            .email(email)
            .major(major)
            .warningTime(0L)
            .build();
    }
}
