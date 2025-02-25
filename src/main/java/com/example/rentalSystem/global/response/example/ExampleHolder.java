package com.example.rentalSystem.global.response.example;

import io.swagger.v3.oas.models.examples.Example;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ExampleHolder {

    private Example holder;
    private String name;
    private int code;

}
