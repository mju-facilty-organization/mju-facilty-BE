package com.example.rentalSystem.domain.rentalhistory.entity;

import com.example.rentalSystem.domain.common.BaseTimeEntity;
import com.example.rentalSystem.domain.professor.entity.Professor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private RentalHistory rentalHistory;

    @ManyToOne
    private Professor professor;

    @Column
    private String reason;

    @Column
    @Enumerated(EnumType.STRING)
    private RentalApplicationResult rentalApplicationResult;

    public String getProfessorName() {
        return professor.getName();
    }

    public String getAffiliationType() {
        return professor.getAffiliationType().getName();
    }

    public String getProfessorEmail() {
        return professor.getEmail();
    }
}

