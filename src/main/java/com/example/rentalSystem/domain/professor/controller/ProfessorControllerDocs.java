package com.example.rentalSystem.domain.professor.controller;

import com.example.rentalSystem.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;

@Tag(name = "교수 관련 API", description = "교수 전체 조회 API")
public interface ProfessorControllerDocs {

    @Operation(summary = "교수 전체 조회 API")
    ApiResponse<?> getAllProfessor(
        @Parameter(example = "{\n"
            + "  \"page\": 0,\n"
            + "  \"size\": 10\n"
            + "}") Pageable pageable,
        @Parameter(example = "서울") String campus,
        @Parameter(example = "ICT융합대학") String college,
        @Parameter(example = "응용소프트웨어전공") String major);

}
