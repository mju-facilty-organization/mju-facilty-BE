package com.example.rentalSystem.domain.student.dto.response;


public record StudentResponse(String studentName) {

    public static StudentResponse from(String studentName) {
        return new StudentResponse(studentName);
    }
}
