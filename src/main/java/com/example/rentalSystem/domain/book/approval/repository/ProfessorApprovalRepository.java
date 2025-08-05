package com.example.rentalSystem.domain.book.approval.repository;

import com.example.rentalSystem.domain.book.approval.entity.ProfessorApproval;
import com.example.rentalSystem.domain.book.rentalhistory.entity.RentalHistory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorApprovalRepository extends JpaRepository<ProfessorApproval, Long> {

    Optional<ProfessorApproval> findByRentalHistory(RentalHistory rentalHistory);
}
