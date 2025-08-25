// src/main/java/com/example/rentalSystem/domain/facility/exception/FacilityExceptionHandler.java
package com.example.rentalSystem.domain.facility.exception;

import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.type.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.example.rentalSystem.domain.facility")
public class FacilityExceptionHandler {

  @ExceptionHandler(FacilityConflictException.class)
  public ResponseEntity<ApiResponse<?>> handleConflict(FacilityConflictException e) {
    log.warn("Facility conflict: {}", e.getMessage());
    // ApiResponse에 data 싣는 오버로드가 없다면 하나 추가해두는 걸 권장
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(ApiResponse.error(ErrorType.DUPLICATE_RESOURCE, e.getMessage(), e.getConflictFacility()));
  }
}