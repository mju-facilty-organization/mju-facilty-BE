package com.example.rentalSystem.domain.book.rentalhistory.repository;

import com.example.rentalSystem.domain.member.student.entity.Student;
import com.example.rentalSystem.domain.book.rentalhistory.entity.RentalHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalHistoryRepository extends JpaRepository<RentalHistory, Long> {

    List<RentalHistory> findAllByStudent(Student student);
}
