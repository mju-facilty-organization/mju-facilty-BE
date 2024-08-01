package com.example.rentalSystem.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessType {
  SUCCESS(200, "[SUCCESS] 요청에 성공하였습니다.");

  private final int httpStatusCode;
  private final String message;
}
