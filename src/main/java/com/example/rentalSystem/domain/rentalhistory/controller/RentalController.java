package com.example.rentalSystem.domain.rentalhistory.controller;

import com.example.rentalSystem.domain.member.entity.CustomerDetails;
import com.example.rentalSystem.domain.rentalhistory.controller.dto.request.CreateRentalRequest;
import com.example.rentalSystem.domain.rentalhistory.controller.dto.response.RentalHistoryDetailResponseDto;
import com.example.rentalSystem.domain.rentalhistory.controller.dto.response.RentalHistoryResponseDto;
import com.example.rentalSystem.domain.rentalhistory.service.RentalService;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.type.SuccessType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rental")
public class RentalController {

    final RentalService rentalService;

    @PostMapping()
    public ApiResponse<?> createRental(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @RequestBody CreateRentalRequest createRentalRequest) {
        rentalService.create(customerDetails.getStudent(), createRentalRequest);
        return ApiResponse.success(SuccessType.CREATED);
    }

    @GetMapping()
    public ApiResponse<Page<RentalHistoryResponseDto>> getAllRentalHistory(
        Pageable pageable) {
        Page<RentalHistoryResponseDto> rentalHistoryResponseDtoList = rentalService.getAllRentalHistory(
            pageable);

        return ApiResponse.success(SuccessType.SUCCESS, rentalHistoryResponseDtoList);
    }

    @GetMapping("/students/{studentId}")
    public ApiResponse<?> getAllRentalHistoryByStudent(
        @PathVariable(name = "studentId") String studentId) {
        List<RentalHistoryResponseDto> rentalHistoryResponseDtoList = rentalService.getAllRentalHistoryByStudentId(
            studentId);
        return ApiResponse.success(SuccessType.SUCCESS, rentalHistoryResponseDtoList);
    }

    @GetMapping("/{rentalHistoryId}")
    public ApiResponse<?> getRentalHistoryDetail(
        @PathVariable(name = "rentalHistoryId") String rentalHistoryId
    ) {
        RentalHistoryDetailResponseDto rentalHistoryDetailResponseDto = rentalService.getRentalHistoryById(
            rentalHistoryId);
        return ApiResponse.success(SuccessType.SUCCESS, rentalHistoryDetailResponseDto);
    }
}
