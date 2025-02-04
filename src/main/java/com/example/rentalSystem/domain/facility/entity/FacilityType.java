package com.example.rentalSystem.domain.facility.entity;

import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ErrorType;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
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

    private final String value;

    //    @JsonCreator
//    public static FacilityType fromKoreanName(String value) {
//        return Arrays.stream(values())
//            .filter(type -> type.value.equals(value))
//            .findAny()
//            .orElseThrow(() -> new CustomException(ErrorType.INVALID_REQUEST));
//    }
    public static String existsByValue(String value) {
        return Arrays.stream(values())
            .filter(type -> type.value.equals(value))
            .map(type -> type.value) // 존재하면 value 반환
            .findFirst()
            .orElseThrow(() -> new CustomException(ErrorType.INVALID_FACILITY_TYPE)); // 없으면 예외 발생
    }

}
