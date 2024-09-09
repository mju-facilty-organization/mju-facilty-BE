package com.example.rentalSystem.domain.student.dto.request;

import com.example.rentalSystem.domain.student.entity.Student;

public record StudentSignUpRequest(
    String name,
    String studentNumber,
    String password,
    String email,
    String affiliation,
    String loginId,
    String major,
    String phoneNumber) {

    public Student toEntity(String encodePassword) {
        return Student.builder()
            .name(name)
            .password(encodePassword)
            .phoneNumber(phoneNumber)
            .affiliation(affiliation)
            .loginId(loginId)
            .studentNumber(studentNumber)
            .email(email)
            .major(major)
            .warningTime(0L)
            .build();
    }
}
