package com.example.rentalSystem.domain.rentalhistory.dto;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.RentalStatus;
import com.example.rentalSystem.domain.member.entity.Member;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalApplicationResult;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.student.entity.Student;
import java.time.LocalDateTime;
import java.util.List;

public record CreateRentalRequest(
    String facilityId,
    LocalDateTime startTime,
    LocalDateTime endTime,
    String organization,
    String numberOfPeople,
    String professorId,
    String purpose
) {

    public RentalHistory toEntity(Student student, Facility facility) {
        return RentalHistory.builder()
            .purpose(this.purpose)
            .organization(this.organization)
            .rentalStartDate(startTime)
            .rentalEndDate(endTime)
            .result(RentalApplicationResult.WAITING)
            .student(student)
            .facility(facility)
            .build();
    }
}
