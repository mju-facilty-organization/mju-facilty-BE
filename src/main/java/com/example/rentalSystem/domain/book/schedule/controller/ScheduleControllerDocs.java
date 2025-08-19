package com.example.rentalSystem.domain.book.schedule.controller;

import com.example.rentalSystem.domain.book.schedule.dto.request.CreateRegularScheduleRequest;
import com.example.rentalSystem.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "스케줄 관리 API", description = "시설 스케줄을 관리하는 API입니다.")
@RequestMapping(value = "/admin/schedules", produces = "application/json;charset=utf-8")
public interface ScheduleControllerDocs {

    @Operation(summary = "정기 스케줄 생성", description = "정기 시설 스케줄을 생성합니다.")
    @PostMapping("/regular")
    ApiResponse<?> createRegularFacility(
            @Parameter(description = "정기 스케줄 생성 요청 데이터", required = true)
            @Valid @RequestBody CreateRegularScheduleRequest request
    );
}
