package com.example.rentalSystem.domain.student.entity;

import com.example.rentalSystem.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
public class Student extends Member {

    @Column(nullable = false)
    private String studentNumber;

    private String major;

    private Long warningTime;
}
