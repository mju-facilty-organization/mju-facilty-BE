package com.example.rentalSystem.domain.professor;

import com.example.rentalSystem.domain.professor.entity.Professor;
import com.example.rentalSystem.domain.professor.repository.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfessorImpl {

    final ProfessorRepository professorRepository;

    public Professor findById(String id) {
        return professorRepository.findById(Long.parseLong(id))
            .orElseThrow(EntityNotFoundException::new);
    }
}
