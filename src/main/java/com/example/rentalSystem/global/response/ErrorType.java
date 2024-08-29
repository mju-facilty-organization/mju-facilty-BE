package com.example.rentalSystem.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

  BAD_REQUEST(400, "잘못된 요청입니다."),
  ENTITY_NOT_FOUND(400, "요청한 정보로 엔터티를 찾을 수 없습니다."),
  FAIL_AUTHENTICATION(401,"아이디와 비밀번호가 틀렸습니다."),
  EXPIRED_ACCESS_TOKEN(400, "만료된 엑세스 토큰입니다."),
  EXPIRED_REFRESH_TOKEN(400,"만료된 리프레쉬 토큰입니다.");

  private final int httpStatusCode;
  private final String message;
}
