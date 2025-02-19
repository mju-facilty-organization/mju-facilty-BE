package com.example.rentalSystem.domain.rentalhistory.implement;

import com.example.rentalSystem.domain.rentalhistory.entity.ProfessorHistory;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.rentalhistory.repository.ProfessorHistoryRepository;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfessorHistoryImpl {

    final ProfessorHistoryRepository professorHistoryRepository;

    public ProfessorHistory save(ProfessorHistory professorHistory) {
        return professorHistoryRepository.save(professorHistory);
    }

    public ProfessorHistory findByRentalHistory(RentalHistory rentalHistory) {
        return professorHistoryRepository.findByRentalHistory(rentalHistory)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }
}
