package com.example.rentalSystem.domain.facility.entity;

import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ErrorType;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum FacilityType {
    MAIN_BUILDING("본관"),
    INTERNATIONAL_BUILDING("국제관"),
    STUDENT_HALL("학생회관"),
    MCC("mcc"),
    LIBRARY("도서관"),
    OUTDOOR("야외");

    private final String koreanName;

    @JsonCreator
    public static FacilityType fromKoreanName(String koreanNane) {
        return Arrays.stream(values())
            .filter(type -> type.koreanName.equals(koreanNane))
            .findAny()
            .orElseThrow(() -> new CustomException(ErrorType.INVALID_REQUEST));
    }
}
