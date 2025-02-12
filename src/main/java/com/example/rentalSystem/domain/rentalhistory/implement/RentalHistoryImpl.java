package com.example.rentalSystem.domain.rentalhistory.implement;

import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.rentalhistory.repository.RentalHistoryRepository;
import com.example.rentalSystem.domain.student.entity.Student;
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

    public RentalHistory findById(String id) {
        return rentalHistoryRepository.findById(Long.parseLong(id))
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

