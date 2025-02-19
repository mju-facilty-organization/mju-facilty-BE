package com.example.rentalSystem.global.exception.custom;

import com.example.rentalSystem.global.response.type.ErrorType;
import lombok.Getter;


@Getter
public class CustomException extends RuntimeException {

    private final ErrorType errorType;

    public CustomException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
