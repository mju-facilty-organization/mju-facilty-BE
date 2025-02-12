package com.example.rentalSystem.domain.rentalhistory.dto.request;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.professor.entity.Professor;
import com.example.rentalSystem.domain.rentalhistory.entity.ProfessorHistory;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalApplicationResult;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.student.entity.Student;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
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
            .rentalApplicationResult(RentalApplicationResult.WAITING)
            .student(student)
            .facility(facility)
            .build();
    }

    public ProfessorHistory toEntity(RentalHistory rentalHistory, Professor professor) {
        return ProfessorHistory.builder()
            .rentalHistory(rentalHistory)
            .professor(professor)
            .build();
    }
}
