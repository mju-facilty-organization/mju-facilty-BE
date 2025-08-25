package com.example.rentalSystem.global.exception;

import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.type.ErrorType;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ApiResponse<?> handleException(Exception e) {
    log.warn(e.getMessage(), e);
    return ApiResponse.error(ErrorType.BAD_REQUEST);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ApiResponse<?> handleEntityNotFoundException(EntityNotFoundException e) {
    log.warn(e.getMessage(), e);
    return ApiResponse.error(ErrorType.ENTITY_NOT_FOUND, e.getMessage());
  }

  @ExceptionHandler(CustomException.class)
  protected ApiResponse<?> handleBusinessException(CustomException e) {
    log.error("CustomException", e);
    String message = (e.getMessage() != null) ? e.getMessage() : e.getErrorType().getMessage();
    return ApiResponse.error(e.getErrorType(), message, e.getData());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ApiResponse<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
    String errorMessage = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getDefaultMessage())
        .findFirst()
        .orElse("잘못된 요청입니다.");
    return ApiResponse.error(ErrorType.INVALID_REQUEST, errorMessage);
  }
}
