package com.example.rentalSystem.domain.member.professor.implement;

import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import com.example.rentalSystem.domain.member.professor.entity.Professor;
import com.example.rentalSystem.domain.member.professor.repository.ProfessorRepository;
import com.example.rentalSystem.domain.professor.entity.QProfessor;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfessorManager {

    final ProfessorRepository professorRepository;

    public Professor findById(String id) {
        return professorRepository.findById(Long.parseLong(id))
            .orElseThrow(EntityNotFoundException::new);
    }

    public Page<Professor> findByAll(Pageable pageable, String campus, String college,
        String major) {
        BooleanBuilder builder = new BooleanBuilder();
        QProfessor professor = QProfessor.professor;

        if (campus != null) {
            builder.and(professor.campusType.eq(AffiliationType.getInstance(campus)));
        }
        if (college != null) {
            builder.and(professor.college.eq(AffiliationType.getInstance(college)));
        }
        if (major != null) {
            builder.and(professor.major.eq(AffiliationType.getInstance(major)));
        }

        return professorRepository.findAll(builder, pageable);
    }
}
