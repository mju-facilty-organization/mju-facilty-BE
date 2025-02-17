package com.example.rentalSystem.domain.professor.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProfessorRepositoryImpl {

    private final JPAQueryFactory jpaQueryFactory;

}
