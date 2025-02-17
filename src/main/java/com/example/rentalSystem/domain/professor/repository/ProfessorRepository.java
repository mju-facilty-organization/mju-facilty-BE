package com.example.rentalSystem.domain.professor.repository;

import com.example.rentalSystem.domain.professor.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long>,
    QuerydslPredicateExecutor<Professor> {

}
