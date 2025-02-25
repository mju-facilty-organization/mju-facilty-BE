package com.example.rentalSystem.domain.professor.controller;

import com.example.rentalSystem.domain.professor.controller.dto.response.ProfessorDetailResponse;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.type.SuccessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "교수 관련 API", description = "교수 전체 조회 API")
public interface ProfessorControllerDocs {

    @Operation(summary = "교수 전체 조회 API")
    ApiResponse<?> getAllProfessor(Pageable pageable, String campus, String college,
        String major);

}
