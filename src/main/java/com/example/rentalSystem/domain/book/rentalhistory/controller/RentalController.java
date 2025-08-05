package com.example.rentalSystem.domain.book.rentalhistory.controller;

import com.example.rentalSystem.domain.book.rentalhistory.dto.request.CreateRentalRequest;
import com.example.rentalSystem.domain.book.rentalhistory.dto.response.RentalHistoryDetailResponseDto;
import com.example.rentalSystem.domain.book.rentalhistory.dto.response.RentalHistoryResponseDto;
import com.example.rentalSystem.domain.book.rentalhistory.service.RentalService;
import com.example.rentalSystem.global.auth.security.CustomerDetails;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.type.SuccessType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
public class RentalController implements RentalControllerDocs {

    final RentalService rentalService;

    @Override
    @PostMapping()
    public ApiResponse<?> createRental(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @RequestBody CreateRentalRequest createRentalRequest) {
        rentalService.create(customerDetails.getStudent(), createRentalRequest);
        return ApiResponse.success(SuccessType.CREATED);
    }

    @Override
    @GetMapping()
    public ApiResponse<Page<RentalHistoryResponseDto>> getAllRentalHistory(
        @PageableDefault Pageable pageable) {
        Page<RentalHistoryResponseDto> rentalHistoryResponseDtoList = rentalService.getAllRentalHistory(
            pageable);

        return ApiResponse.success(SuccessType.SUCCESS, rentalHistoryResponseDtoList);
    }

    @Override
    @GetMapping("/students/{studentId}")
    public ApiResponse<?> getAllRentalHistoryByStudent(
        @PathVariable(name = "studentId") String studentId) {
        List<RentalHistoryResponseDto> rentalHistoryResponseDtoList = rentalService.getAllRentalHistoryByStudentId(
            studentId);
        return ApiResponse.success(SuccessType.SUCCESS, rentalHistoryResponseDtoList);
    }

    @Override
    @GetMapping("/{rentalHistoryId}")
    public ApiResponse<?> getRentalHistoryDetail(
        @PathVariable(name = "rentalHistoryId") Long rentalHistoryId
    ) {
        RentalHistoryDetailResponseDto rentalHistoryDetailResponseDto = rentalService.getRentalHistoryDetailById(
            rentalHistoryId);
        return ApiResponse.success(SuccessType.SUCCESS, rentalHistoryDetailResponseDto);
    }
}
