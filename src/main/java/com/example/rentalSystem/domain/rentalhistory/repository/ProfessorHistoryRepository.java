package com.example.rentalSystem.domain.rentalhistory.repository;

import com.example.rentalSystem.domain.rentalhistory.entity.ProfessorHistory;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorHistoryRepository extends JpaRepository<ProfessorHistory, Long> {

    Optional<ProfessorHistory> findByRentalHistory(RentalHistory rentalHistory);
}
