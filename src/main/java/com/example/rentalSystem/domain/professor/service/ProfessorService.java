package com.example.rentalSystem.domain.professor.service;

import com.example.rentalSystem.domain.professor.ProfessorManager;
import com.example.rentalSystem.domain.professor.controller.dto.response.ProfessorDetailResponse;
import com.example.rentalSystem.domain.professor.entity.Professor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorManager professorManager;

    public Page<ProfessorDetailResponse> getAllProfessorDetail(Pageable pageable, String campus,
        String college,
        String major) {
        Page<Professor> professorList = professorManager.findByAll(pageable, campus, college,
            major);
        return professorList.map(ProfessorDetailResponse::from);
    }
}
