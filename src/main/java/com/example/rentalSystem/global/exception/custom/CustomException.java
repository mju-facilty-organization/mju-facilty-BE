package com.example.rentalSystem.global.exception.custom;

import com.example.rentalSystem.global.response.type.ErrorType;
import lombok.Getter;


@Getter
public class CustomException extends RuntimeException {

  private final ErrorType errorType;
  private final Object data;

  public CustomException(ErrorType errorType) {
    super(errorType.getMessage());
    this.errorType = errorType;
    this.data = null;
  }

  public CustomException(ErrorType errorType, String message) {
    super(message);
    this.errorType = errorType;
    this.data = null;
  }

  public CustomException(ErrorType errorType, Object data) {
    super(errorType.getMessage());
    this.errorType = errorType;
    this.data = data;
  }
}
