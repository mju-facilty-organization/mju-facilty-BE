package com.example.rentalSystem.domain.member.student.controller.dto.response;


import com.example.rentalSystem.domain.member.student.entity.Student;
import lombok.Builder;

@Builder
public record StudentInfoResponse(
    String studentName,
    String studentNumber,
    String email,
    String major,
    String phoneNumber,
    String status,
    String warning
) {

    public static StudentInfoResponse toSimpleStudentResponse(Student student) {
        return StudentInfoResponse.builder()
            .studentName(student.getName())
            .build();
    }

    public static StudentInfoResponse toStudentInfoResponse(Student student) {
        return StudentInfoResponse.builder()
            .studentName(student.getName())
            .studentNumber(student.getStudentNumber())
            .email(student.getEmail())
            .major(student.getMajorName())
            .phoneNumber(student.getPhoneNumber())
            .status("정상")
            .warning("0")
            .build();
    }
}
