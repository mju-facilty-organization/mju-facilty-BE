package com.example.rentalSystem.global.response;

import org.springframework.http.HttpStatus;

public class HttpStatusCode {

  // 요청에 성공하였을 경우 사용합니다
  public static Integer OK = HttpStatus.OK.value();

  //  인증/인가 자체가 유효하지 않은 경우 사용합니다.
  public static Integer UNAUTHORIZED = HttpStatus.UNAUTHORIZED.value();

  // 인증 자체는 성공적이나, 권한이 존재하지 않는 경우 사용합니다.
  public static Integer FORBIDDEN = HttpStatus.FORBIDDEN.value();

  // 유저의 요청이 정상적이지 않은 경우 사용합니다.
  public static Integer BAD_REQUEST = HttpStatus.BAD_REQUEST.value();

  // 명령을 처리할 개체가 존재하지 않는 경우 사용합니다.
  public static Integer NOT_FOUND = HttpStatus.NOT_FOUND.value();

  // 서버가 기절했을때 사용합니다.
  public static Integer INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.value();
}
