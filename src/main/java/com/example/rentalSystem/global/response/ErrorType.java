package com.example.rentalSystem.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

  BAD_REQUEST(400, "[ERROR] 잘못된 요청입니다.");

  private final int httpStatusCode;
  private final String message;
}
