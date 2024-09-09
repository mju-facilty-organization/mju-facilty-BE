package com.example.rentalSystem.domain.student.entity;

import com.example.rentalSystem.domain.member.entity.Member;
import com.example.rentalSystem.domain.member.entity.Role;
import com.example.rentalSystem.domain.student.dto.request.StudentUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Student extends Member {

    @Column(nullable = false)
    private String studentNumber;

    private String major;

    private Long warningTime;

    @Builder
    public Student(String studentNumber, String major, Long warningTime,
        String email, String loginId, String name,
        String password, String phoneNumber, String affiliation) {
        super(email, loginId, Role.STUDENT, name, password, phoneNumber, affiliation);
        this.studentNumber = studentNumber;
        this.major = major;
        this.warningTime = warningTime != null ? warningTime : 0L;  // 기본값 설정
    }

    public void updateInfo(String name, String major) {
        this.major = major;
        this.name = name;
    }

}
