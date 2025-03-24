package com.example.rentalSystem.domain.rentalhistory.controller.dto.response;

import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;

public record RentalInfoResponse(
    String date,
    String time,
    int numberOfPeople
) {

    public static RentalInfoResponse of(
        RentalHistory rentalHistory,
        String date,
        String time
    ) {
        return new RentalInfoResponse(
            date,
            time,
            rentalHistory.getNumberOfPeople()
        );
    }
}
