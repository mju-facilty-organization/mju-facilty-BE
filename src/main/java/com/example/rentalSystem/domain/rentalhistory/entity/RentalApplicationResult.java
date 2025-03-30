package com.example.rentalSystem.domain.rentalhistory.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RentalApplicationResult {

    PIC_PERMITTED("교직원 승인 허가"),
    PROFESSOR_PERMITTED("교수 승인 허가"),
    WAITING("대기중"),
    PIC_DENIED("교직원 승인 거절"),
    PROFESSOR_DENIED("교수 승인 거절");

    private final String description;
}
