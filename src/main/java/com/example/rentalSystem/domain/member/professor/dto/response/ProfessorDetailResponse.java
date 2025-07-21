package com.example.rentalSystem.domain.member.professor.dto.response;

import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import com.example.rentalSystem.domain.member.professor.entity.Professor;

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
