package com.example.rentalSystem.domain.professor.controller.dto.response;

import com.example.rentalSystem.domain.affiliation.type.AffiliationType;
import com.example.rentalSystem.domain.professor.entity.Professor;

public record ProfessorDetailResponse(
    long id,
    String name,
    AffiliationType campus,
    AffiliationType college,
    AffiliationType major,
    String email
) {

    public static ProfessorDetailResponse from(Professor professor) {
        return new ProfessorDetailResponse(
            professor.getId(),
            professor.getName(),
            professor.getCampusType(),
            professor.getCollege(),
            professor.getMajor(),
            professor.getEmail()
        );
    }
}
