package com.example.rentalSystem.domain.professor.entity;

import com.example.rentalSystem.domain.affiliation.converter.AffiliationConverter;
import com.example.rentalSystem.domain.affiliation.type.AffiliationType;
import com.example.rentalSystem.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Professor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Convert(converter = AffiliationConverter.class)
    private AffiliationType campusType;

    @Column(nullable = false)
    @Convert(converter = AffiliationConverter.class)
    private AffiliationType college;

    @Column(nullable = false)
    @Convert(converter = AffiliationConverter.class)
    private AffiliationType major;

    @Column(nullable = false)
    private String email;

    @Builder
    public Professor(String name, AffiliationType campusType, AffiliationType college,
        AffiliationType major,
        String email) {
        this.name = name;
        this.campusType = campusType;
        this.college = college;
        this.major = major;
        this.email = email;
    }
}
