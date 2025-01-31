package com.example.rentalSystem.domain.rentalhistory.repository;

import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalHistoryRepository extends JpaRepository<RentalHistory, Long> {

}
