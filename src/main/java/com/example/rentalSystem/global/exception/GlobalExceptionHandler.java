package com.example.rentalSystem.global.exception;

import com.example.rentalSystem.global.response.ErrorType;
import com.example.rentalSystem.global.response.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionResponse handleException(Exception e) {
    log.warn(e.getMessage(), e);
    return ExceptionResponse.of(ErrorType.BAD_REQUEST);
  }
}
