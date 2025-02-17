package com.example.rentalSystem.domain.student.controller.dto.response;


public record StudentSignUpResponse(String studentName) {

    public static StudentSignUpResponse from(String name) {
        return new StudentSignUpResponse(name);
    }
}
