package com.example.rentalSystem.domain.suggestion.repositry;

import com.example.rentalSystem.domain.member.student.entity.Student;
import com.example.rentalSystem.domain.suggestion.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuggestionJpaRepository extends JpaRepository<Suggestion, Long> {
    List<Suggestion> findByStudent(Student student);
}