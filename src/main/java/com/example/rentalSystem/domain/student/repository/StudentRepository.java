package com.example.rentalSystem.domain.student.repository;

import com.example.rentalSystem.domain.student.entity.Student;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);
}
