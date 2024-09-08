package com.example.rentalSystem.domain.student.entity;

import com.example.rentalSystem.domain.member.entity.Member;
import com.example.rentalSystem.domain.student.dto.request.StudentSignUpRequest;
import com.example.rentalSystem.domain.student.dto.request.StudentUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@AllArgsConstructor
@Getter
public class Student extends Member {

    @Column(nullable = false)
    private String studentNumber;

    private String major;

    private Long warningTime;

    public void updateInfo(StudentUpdateRequest studentUpdateRequest) {
        major = studentUpdateRequest.major();
        name = studentUpdateRequest.name();
    }
}
