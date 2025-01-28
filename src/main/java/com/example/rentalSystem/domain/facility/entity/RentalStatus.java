package com.example.rentalSystem.domain.facility.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum RentalStatus {
    AVAILABLE("예약가능"), UNAVAILABLE("이용불가"), RESERVED("예약완료"), WAITING("예약대기"), CURRENT("현재예약");
    final String description;
}
