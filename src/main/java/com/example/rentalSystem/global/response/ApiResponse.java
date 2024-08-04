package com.example.rentalSystem.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

@Builder
@JsonPropertyOrder({"httpStatusCode", "message", "data"})
public record ApiResponse<T>(
    ResultType resultType,
    int httpStatusCode,
    String message,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    T data
) {

  public static <T> ApiResponse<T> success(SuccessType successType, T data) {
    return ApiResponse.<T>builder()
        .resultType(ResultType.SUCCESS)
        .httpStatusCode(successType.getHttpStatusCode())
        .message(successType.getMessage())
        .data(data)
        .build();
  }

  public static ApiResponse<?> success(SuccessType successType) {
    return success(successType, null);
  }

  public static <T> ApiResponse<T> error(ErrorType errorType, String message, T data) {
    return ApiResponse.<T>builder()
        .resultType(ResultType.FAIL)
        .httpStatusCode(errorType.getHttpStatusCode())
        .message(message)
        .data(data)
        .build();
  }

  public static ApiResponse<?> error(ErrorType errorType) {
    return error(errorType, errorType.getMessage(), null);
  }

  public static ApiResponse<?> error(ErrorType errorType, String message) {
    return error(errorType, message, null);
  }

  public static <T> ApiResponse<T> error(ErrorType errorType, T data) {
    return error(errorType, errorType.getMessage(), data);
  }
}
