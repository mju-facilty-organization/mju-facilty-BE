package com.example.rentalSystem.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

  BAD_REQUEST(400, "잘못된 요청입니다."),
  ENTITY_NOT_FOUND(400, "요청한 정보로 엔터티를 찾을 수 없습니다.");

  private final int httpStatusCode;
  private final String message;
}
