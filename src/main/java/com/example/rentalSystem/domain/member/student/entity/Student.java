package com.example.rentalSystem.domain.member.student.entity;

import com.example.rentalSystem.domain.common.convert.AffiliationConverter;
import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import com.example.rentalSystem.domain.member.base.entity.Member;
import com.example.rentalSystem.domain.member.base.entity.type.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

    @Convert(converter = AffiliationConverter.class)
    private AffiliationType major;

    private Long warningTime;

    @Builder
    public Student(String studentNumber, String major, Long warningTime,
        String email, String name,
        String password, String phoneNumber) {
        super(email, Role.STUDENT, name, password, phoneNumber,
            AffiliationType.getCollegeByMajor(major));
        this.studentNumber = studentNumber;
        this.major = AffiliationType.getInstance(major);
        this.warningTime = warningTime != null ? warningTime : 0L;  // 기본값 설정
    }

    public void updateInfo(String name, String major) {
        this.major = AffiliationType.getInstance(major);
        this.name = name;
    }

    public String getMajorName() {
        return major.getName();
    }
}
