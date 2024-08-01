package com.example.rentalSystem.domain.rentalhistory.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RentalApplicationResult {

  PERMITTED("허가 완료"),
  WAITING("대기중"),
  DENIED("거절됨");

  private final String description;
}
