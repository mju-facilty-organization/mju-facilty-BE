package com.example.rentalSystem.domain.rentalhistory.entity;

import static com.example.rentalSystem.domain.rentalhistory.entity.RentalApplicationResult.PIC_DENIED;
import static com.example.rentalSystem.domain.rentalhistory.entity.RentalApplicationResult.PROFESSOR_DENIED;

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
    private LocalDateTime rentalStartDateTime;

    @Column(nullable = false)
    private LocalDateTime rentalEndDateTime;

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
        LocalDateTime rentalStartDateTime,
        LocalDateTime rentalEndDateTime,
        RentalApplicationResult rentalApplicationResult,
        Student student,
        int numberOfPeople,
        Facility facility
    ) {
        this.purpose = purpose;
        this.organization = organization;
        this.rentalStartDateTime = rentalStartDateTime;
        this.rentalEndDateTime = rentalEndDateTime;
        this.rentalApplicationResult = rentalApplicationResult;
        this.student = student;
        this.facility = facility;
        this.numberOfPeople = numberOfPeople;
    }

    public void registerApplicationResult(
        Pic pic,
        RegisterRentalResultRequest registerRentalResultRequest
    ) {
        this.pic = pic;
        this.rentalApplicationResult = registerRentalResultRequest.rentalApplicationResult();
        this.defineDateTime = LocalDateTime.now();

        if (this.rentalApplicationResult == PIC_DENIED) {
            this.reason = registerRentalResultRequest.reason();
        }
    }

    public void registerApplicationResult(
        RentalApplicationResult rentalApplicationResult
    ) {

        this.rentalApplicationResult = rentalApplicationResult;
        this.defineDateTime = LocalDateTime.now();

        if (this.rentalApplicationResult == PROFESSOR_DENIED) {
            this.reason = "교수님의 승인 거절";
        }
    }
}
