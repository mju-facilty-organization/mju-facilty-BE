package com.example.rentalSystem.domain.student.dto.response;


import com.example.rentalSystem.domain.student.entity.Student;

public record StudentResponse(String studentName) {

    public static StudentResponse from(String studentName) {
        return new StudentResponse(studentName);
    }

    public static StudentResponse toStudentResponse(Student student) {
        return new StudentResponse(student.getName());
    }
}
