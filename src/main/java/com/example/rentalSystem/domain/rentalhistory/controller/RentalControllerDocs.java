package com.example.rentalSystem.domain.rentalhistory.controller;

import static com.example.rentalSystem.global.response.type.ErrorType.ENTITY_NOT_FOUND;
import static com.example.rentalSystem.global.response.type.ErrorType.FAIL_RENTAL_REQUEST;
import static com.example.rentalSystem.global.response.type.ErrorType.FAIL_SEND_EMAIL;

import com.example.rentalSystem.domain.member.entity.CustomerDetails;
import com.example.rentalSystem.domain.rentalhistory.controller.dto.request.CreateRentalRequest;
import com.example.rentalSystem.domain.rentalhistory.controller.dto.response.RentalHistoryResponseDto;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.example.ApiErrorCodeExample;
import com.example.rentalSystem.global.response.example.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Tag(name = "시설 대여에 관한 API", description = "시설 대여, 전체 대여 내역 조회, 시설 대여 내역 상세 조회, 학생 시설 대여 내역 조회")
public interface RentalControllerDocs {

    @Operation(summary = "시설 대여 신청")
    @ApiErrorCodeExamples(
        {FAIL_RENTAL_REQUEST, ENTITY_NOT_FOUND, FAIL_SEND_EMAIL}
    )
    ApiResponse<?> createRental(
        CustomerDetails customerDetails,
        CreateRentalRequest createRentalRequest);

    @Operation(summary = "시설 대여 내역 전체 조회")
    ApiResponse<Page<RentalHistoryResponseDto>> getAllRentalHistory(
        @Parameter(example = "{\n"
            + "  \"page\": 0,\n"
            + "  \"size\": 10\n"
            + "}")
        Pageable pageable);

    @Operation(summary = "학생 시설 대여 내역 전체 조회")
    ApiResponse<?> getAllRentalHistoryByStudent(
        String studentId);

    @Operation(summary = "시설 대여 내역 상세 조회")
    @ApiErrorCodeExample(ENTITY_NOT_FOUND)
    ApiResponse<?> getRentalHistoryDetail(
        Long rentalHistoryId
    );
}
