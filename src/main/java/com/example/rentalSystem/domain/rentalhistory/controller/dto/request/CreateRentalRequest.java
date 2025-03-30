package com.example.rentalSystem.domain.rentalhistory.controller.dto.request;

import com.example.rentalSystem.domain.approval.entity.ProfessorApproval;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.professor.entity.Professor;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalApplicationResult;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.student.entity.Student;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CreateRentalRequest(
    @Schema(example = "1")
    String facilityId,
    @Schema(description = "대여 시작 시간", example = "YYYY-MM-DDTHH:MM:SS")
    LocalDateTime startDateTime,
    @Schema(description = "대역 종료 시간", example = "YYYY-MM-DDTHH:MM:SS")
    LocalDateTime endDateTime,
    @Schema(description = "조직명", example = "COW")
    String organization,
    @Schema(description = "사용자 수", example = "4")
    int numberOfPeople,
    @Schema(description = "책임 교수 Id", example = "1")
    String professorId,
    @Schema(description = "사용 목적", example = "동아리 활동")
    String purpose
) {

    public RentalHistory toEntity(Student student, Facility facility) {
        return RentalHistory.builder()
            .purpose(this.purpose)
            .organization(this.organization)
            .rentalStartDateTime(startDateTime)
            .rentalEndDateTime(endDateTime)
            .rentalApplicationResult(RentalApplicationResult.WAITING)
            .student(student)
            .facility(facility)
            .numberOfPeople(numberOfPeople)
            .build();
    }

    public ProfessorApproval toEntity(RentalHistory rentalHistory, Professor professor) {
        return ProfessorApproval.builder()
            .rentalHistory(rentalHistory)
            .professor(professor)
            .build();
    }
}
