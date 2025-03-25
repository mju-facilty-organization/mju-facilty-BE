package com.example.rentalSystem.domain.rentalhistory.entity;

import static com.example.rentalSystem.domain.rentalhistory.entity.RentalApplicationResult.DENIED;

import com.example.rentalSystem.domain.approval.controller.dto.request.RegisterRentalResultRequest;
import com.example.rentalSystem.domain.common.BaseTimeEntity;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.pic.entity.Pic;
import com.example.rentalSystem.domain.student.entity.Student;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RentalHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rentalHistory_id")
    private Long id;

    @Column(nullable = false)
    private String purpose;

    @Column(nullable = false)
    private String organization;

    @Column(nullable = false)
    private LocalDateTime rentalStartDate;

    @Column(nullable = false)
    private LocalDateTime rentalEndDate;

    @Column
    @Enumerated(EnumType.STRING)
    private RentalApplicationResult rentalApplicationResult;

    @Column
    private LocalDateTime defineDateTime;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Facility facility;

    @ManyToOne
    @JoinColumn(name = "pic_id")
    private Pic pic;

    @Column
    private int numberOfPeople;

    private String reason;

    @Builder
    public RentalHistory(
        String purpose,
        String organization,
        LocalDateTime rentalStartDate,
        LocalDateTime rentalEndDate,
        RentalApplicationResult rentalApplicationResult,
        Student student,
        int numberOfPeople,
        Facility facility
    ) {
        this.purpose = purpose;
        this.organization = organization;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
        this.rentalApplicationResult = rentalApplicationResult;
        this.student = student;
        this.facility = facility;
        this.numberOfPeople = numberOfPeople;
    }

    public void defineApplicationResult(
        Pic pic,
        RegisterRentalResultRequest registerRentalResultRequest
    ) {
        this.pic = pic;
        this.rentalApplicationResult = registerRentalResultRequest.rentalApplicationResult();
        this.defineDateTime = LocalDateTime.now();

        if (this.rentalApplicationResult == DENIED) {
            this.reason = registerRentalResultRequest.reason();
        }
    }
}
