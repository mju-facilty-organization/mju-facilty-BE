package com.example.rentalSystem.global.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

@Builder
@JsonPropertyOrder({"httpStatusCode", "errorCode", "message"})
public record ExceptionResponse(
    int httpStatusCode,
    String errorCode,
    String message
) {

  public static ExceptionResponse of(ErrorType errorType) {
    return ExceptionResponse.builder()
        .httpStatusCode(errorType.getHttpStatusCode())
        .errorCode(errorType.getErrorCode())
        .message(errorType.getMessage())
        .build();
  }
}
