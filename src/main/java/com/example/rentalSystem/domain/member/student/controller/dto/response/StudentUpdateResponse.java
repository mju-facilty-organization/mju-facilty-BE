package com.example.rentalSystem.domain.member.student.controller.dto.response;

public record StudentUpdateResponse(String studentName, String major) {

    public static StudentUpdateResponse of(String studentName, String major) {
        return new StudentUpdateResponse(studentName, major);
    }
}
