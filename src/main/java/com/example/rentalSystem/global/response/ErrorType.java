package com.example.rentalSystem.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

  BAD_REQUEST(HttpStatusCode.BAD_REQUEST, "GLOBAL-001","[ERROR] 잘못된 요청입니다.");

  private final int httpStatusCode;
  private final String errorCode;
  private final String message;
}
