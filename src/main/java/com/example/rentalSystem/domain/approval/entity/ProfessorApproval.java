package com.example.rentalSystem.domain.approval.entity;

import static com.example.rentalSystem.domain.rentalhistory.entity.RentalApplicationResult.DENIED;

import com.example.rentalSystem.domain.affiliation.type.AffiliationType;
import com.example.rentalSystem.domain.approval.controller.dto.request.RegisterRentalResultRequest;
import com.example.rentalSystem.domain.common.BaseTimeEntity;
import com.example.rentalSystem.domain.professor.entity.Professor;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalApplicationResult;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfessorApproval extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public String getProfessorEmail() {
        return professor.getEmail();
    }

    public AffiliationType getProfessorAffiliation() {
        return professor.getMajor();
    }

    public void registerResult(RegisterRentalResultRequest registerRentalResultRequest) {
        if (
            DENIED == registerRentalResultRequest.rentalApplicationResult()
        ) {
            reason = registerRentalResultRequest.reason();
        }
        rentalApplicationResult = registerRentalResultRequest.rentalApplicationResult();
    }
}

