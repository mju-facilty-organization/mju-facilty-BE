package com.example.rentalSystem.domain.rentalhistory.controller.dto.response;

import com.example.rentalSystem.domain.facility.controller.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.rentalhistory.entity.ProfessorHistory;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalApplicationResult;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
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
    private ProfessorHistoryResponse professorHistoryResponse;
    private LocalDateTime createAt;
    @JsonInclude(Include.ALWAYS)
    private LocalDateTime defineDateTime;
    private String organization;
    private String purpose;
    private RentalApplicationResult applicationResult;

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
        ProfessorHistory professorHistory) {
        return RentalHistoryResponseDto.builder()
            .id(rentalHistory.getId())
            .facilityResponse(
                FacilityResponse.fromRentalHistory(rentalHistory))
            .professorHistoryResponse(
                ProfessorHistoryResponse.from(professorHistory))
            .organization(rentalHistory.getOrganization())
            .purpose(rentalHistory.getPurpose())
            .createAt(rentalHistory.getCreated_at())
            .defineDateTime(rentalHistory.getDefineDateTime())
            .applicationResult(rentalHistory.getRentalApplicationResult())
            .build();
    }
}
