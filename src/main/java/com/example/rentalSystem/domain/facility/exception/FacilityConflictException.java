// src/main/java/com/example/rentalSystem/domain/facility/exception/FacilityConflictException.java
package com.example.rentalSystem.domain.facility.exception;

import com.example.rentalSystem.domain.facility.dto.response.FacilityDetailResponse;
import lombok.Getter;

@Getter
public class FacilityConflictException extends RuntimeException {

  private final FacilityDetailResponse conflictFacility;

  public FacilityConflictException(FacilityDetailResponse conflictFacility) {
    super("이미 동일한 시설번호가 존재합니다.");
    this.conflictFacility = conflictFacility;
  }
}