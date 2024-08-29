package com.example.rentalSystem.domain.student.dto.response;



public record StudentSignUpResponse(String studentName) {

    public static StudentSignUpResponse form(String name) {
        return new StudentSignUpResponse(name);
    }
}
