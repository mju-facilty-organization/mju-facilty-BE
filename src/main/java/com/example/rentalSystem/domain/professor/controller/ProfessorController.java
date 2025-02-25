package com.example.rentalSystem.domain.professor.controller;

import com.example.rentalSystem.domain.professor.controller.dto.response.ProfessorDetailResponse;
import com.example.rentalSystem.domain.professor.service.ProfessorService;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.type.SuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/professors")
public class ProfessorController implements ProfessorControllerDocs {

    private final ProfessorService professorService;

    @GetMapping
    public ApiResponse<?> getAllProfessor(
        @PageableDefault Pageable pageable,
        @RequestParam(value = "campus", required = false) String campus,
        @RequestParam(value = "college", required = false) String college,
        @RequestParam(value = "major", required = false) String major
    ) {
        Page<ProfessorDetailResponse> professorResponseDtos = professorService.getAllProfessorDetail(
            pageable, campus, college, major);
        return ApiResponse.success(SuccessType.SUCCESS, professorResponseDtos);
    }
}
