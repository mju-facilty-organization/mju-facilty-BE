package com.example.rentalSystem.domain.rentalhistory.dto.response;

import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalApplicationResult;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record RentalHistoryResponseDto(
    Long id,
    String organization,
    String purpose,
    LocalDateTime createAt,
    @JsonInclude(Include.ALWAYS)
    LocalDateTime defineDateTime,
    RentalApplicationResult result,
    FacilityResponse facilityResponse
) {

    public static RentalHistoryResponseDto from(RentalHistory rentalHistory) {
        return RentalHistoryResponseDto.builder()
            .id(rentalHistory.getId())
            .facilityResponse(
                FacilityResponse.fromRentalHistory(rentalHistory))
            .organization(rentalHistory.getOrganization())
            .purpose(rentalHistory.getPurpose())
            .createAt(rentalHistory.getCreated_at())
            .defineDateTime(rentalHistory.getDefineDateTime())
            .result(rentalHistory.getResult())
            .build();
    }
}
