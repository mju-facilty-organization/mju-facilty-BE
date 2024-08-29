package com.example.rentalSystem.domain.student.dto.request;

import com.example.rentalSystem.domain.student.entity.Student;
import java.time.LocalDateTime;
import lombok.Builder;

public record StudentSignUpRequest(String name, String studentNumber,String password, String email, String affiliation, String loginId,String major, String phoneNumber) {

    public Student toEntity(String encodePassword) {
        return Student.builder()
            .name(name)
            .password(encodePassword)
            .role("ROLE_STUDENT")
            .phoneNumber(phoneNumber)
            .affiliation(affiliation)
            .loginId(loginId)
            .studentNumber(this.studentNumber)
            .email(email)
            .major(major)
            .warningTime(0L)
            .build();
    }
}
