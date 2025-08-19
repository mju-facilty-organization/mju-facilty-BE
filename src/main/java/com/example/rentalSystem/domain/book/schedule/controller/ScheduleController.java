package com.example.rentalSystem.domain.book.schedule.controller;

import com.example.rentalSystem.domain.book.schedule.dto.request.CreateRegularScheduleRequest;
import com.example.rentalSystem.domain.book.schedule.service.ScheduleService;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.type.SuccessType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin/schedules", produces = "application/json;charset=utf-8")
public class ScheduleController implements ScheduleControllerDocs {

    private final ScheduleService scheduleService;

    @PostMapping("/regular")
    public ApiResponse<?> createRegularFacility(
            @Valid @RequestBody CreateRegularScheduleRequest request
    ) {
        scheduleService.createSchedule(request);
        return ApiResponse.success(SuccessType.SUCCESS);
    }
}
