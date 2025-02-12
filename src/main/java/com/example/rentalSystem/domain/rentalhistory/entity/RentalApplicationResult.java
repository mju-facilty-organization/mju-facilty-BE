package com.example.rentalSystem.domain.rentalhistory.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RentalApplicationResult {

    PERMITTED("허가 완료"),
    WAITING("대기중"),
    DENIED("거절됨");

    private final String description;
}
