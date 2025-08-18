package com.example.rentalSystem.domain.facility.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FacilityImportResponseDto {
    private final int total;
    private final int success;
    private final int skipped;
    private final List<String> warnings;
    private final List<String> errors;
}