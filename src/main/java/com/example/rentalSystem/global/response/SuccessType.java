package com.example.rentalSystem.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessType {

  SUCCESS(HttpStatus.OK, HttpStatusCode.OK, "GLOBAL-001", "[SUCCESS] 요청에 성공하였습니다.");

  private final HttpStatus httpStatus;
  private final int httpStatusCode;
  private final String successCode;
  private final String message;
}
