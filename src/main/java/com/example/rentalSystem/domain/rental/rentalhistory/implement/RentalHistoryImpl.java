package com.example.rentalSystem.domain.rental.rentalhistory.implement;

import com.example.rentalSystem.domain.member.student.entity.Student;
import com.example.rentalSystem.domain.rental.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.rental.rentalhistory.repository.RentalHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RentalHistoryImpl {

    private final RentalHistoryRepository rentalHistoryRepository;

    public RentalHistory findById(Long id) {
        return rentalHistoryRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    public RentalHistory save(RentalHistory rentalHistory) {
        return rentalHistoryRepository.save(rentalHistory);
    }

    public Page<RentalHistory> findAll(Pageable pageable) {
        return rentalHistoryRepository.findAll(pageable);
    }

    public List<RentalHistory> findAllByStudent(Student student) {
        return rentalHistoryRepository.findAllByStudent(student);
    }
}

