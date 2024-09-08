package com.example.rentalSystem.domain.student.dto.response;

public record StudentUpdateResponse(String studentName, String major) {

    public static StudentUpdateResponse from(String studentName, String major) {
        return new StudentUpdateResponse(studentName, major);
    }
}
