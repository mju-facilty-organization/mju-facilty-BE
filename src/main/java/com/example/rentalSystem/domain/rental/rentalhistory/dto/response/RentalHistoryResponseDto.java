package com.example.rentalSystem.domain.rental.rentalhistory.dto.response;

import com.example.rentalSystem.domain.rental.approval.dto.response.ProfessorApprovalResponse;
import com.example.rentalSystem.domain.rental.approval.entity.ProfessorApproval;
import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.rental.rentalhistory.entity.RentalApplicationResult;
import com.example.rentalSystem.domain.rental.rentalhistory.entity.RentalHistory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RentalHistoryResponseDto {

    private Long id;
    private FacilityResponse facilityResponse;
    private ProfessorApprovalResponse professorApprovalResponse;
    private LocalDateTime createAt;
    @JsonInclude(Include.ALWAYS)
    private LocalDateTime defineDateTime;
    private String organization;
    private String purpose;
    private RentalApplicationResult applicationResult;

    @JsonInclude(Include.NON_NULL)
    private LocalDateTime startTime;
    @JsonInclude(Include.NON_NULL)
    private LocalDateTime endTime;

    public static RentalHistoryResponseDto from(RentalHistory rentalHistory) {
        return RentalHistoryResponseDto.builder()
            .id(rentalHistory.getId())
            .facilityResponse(
                FacilityResponse.fromRentalHistory(rentalHistory))
            .organization(rentalHistory.getOrganization())
            .purpose(rentalHistory.getPurpose())
            .createAt(rentalHistory.getCreated_at())
            .defineDateTime(rentalHistory.getDefineDateTime())
            .applicationResult(rentalHistory.getRentalApplicationResult())
            .build();
    }

    public static RentalHistoryResponseDto toDetailResponseDto(RentalHistory rentalHistory,
        ProfessorApproval professorApproval) {
        return RentalHistoryResponseDto.builder()
            .id(rentalHistory.getId())
            .facilityResponse(
                FacilityResponse.fromRentalHistory(rentalHistory))
            .professorApprovalResponse(
                ProfessorApprovalResponse.from(professorApproval))
            .startTime(rentalHistory.getRentalStartDateTime())
            .endTime(rentalHistory.getRentalEndDateTime())
            .organization(rentalHistory.getOrganization())
            .purpose(rentalHistory.getPurpose())
            .createAt(rentalHistory.getCreated_at())
            .defineDateTime(rentalHistory.getDefineDateTime())
            .applicationResult(rentalHistory.getRentalApplicationResult())
            .build();
    }
}
