package com.example.rentalSystem.domain.student.dto.response;


import com.example.rentalSystem.domain.student.entity.Student;
import lombok.Builder;

@Builder
public record StudentResponse(
    String studentName,
    String studentNumber,
    String email,
    String major,
    String phoneNumber,
    String status,
    String warning
) {

    public static StudentResponse toSimpleStudentResponse(Student student) {
        return StudentResponse.builder()
            .studentName(student.getName())
            .build();
    }

    public static StudentResponse toStudentResponse(Student student) {
        return StudentResponse.builder()
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
