package com.example.rentalSystem.domain.rentalhistory.entity;

import com.example.rentalSystem.domain.affiliation.type.AffiliationType;
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

    /**
     * Returns the name of the associated professor.
     *
     * @return the professor's name
     */
    public String getProfessorName() {
        return professor.getName();
    }

    /**
     * Returns the email address of the associated professor.
     *
     * @return the professor's email address
     */
    public String getProfessorEmail() {
        return professor.getEmail();
    }

    /**
     * Returns the affiliation type of the associated professor.
     *
     * <p>This value is derived from the professor's major and represents the professor's affiliation 
     * as an {@code AffiliationType}.</p>
     *
     * @return the professor's affiliation type
     */
    public AffiliationType getProfessorAffiliation() {
        return professor.getMajor();
    }
}

