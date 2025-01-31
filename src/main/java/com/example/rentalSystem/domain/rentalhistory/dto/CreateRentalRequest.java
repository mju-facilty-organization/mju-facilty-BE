package com.example.rentalSystem.domain.rentalhistory.dto;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.RentalStatus;
import com.example.rentalSystem.domain.member.entity.Member;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalApplicationResult;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import java.util.List;
import org.joda.time.LocalDateTime;

public record CreateRentalRequest(
    String facilityId,
    String startTime,
    String endTime,
    String organization,
    String numberOfPeople,
    String professorId,
    String purpose
) {

    public RentalHistory toEntity(Member member, Facility facility) {
        return RentalHistory.builder()
            .purpose(this.purpose)
            .organization(this.organization)
            .rentalStartDate(LocalDateTime.parse(startTime))
            .rentalEndDate(LocalDateTime.parse(endTime))
            .result(RentalApplicationResult.WAITING)
            .member(member)
            .facility(facility)
            .build();
    }
}
