package com.example.rentalSystem.domain.member.student.repository;

import com.example.rentalSystem.domain.member.student.entity.Student;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

}
