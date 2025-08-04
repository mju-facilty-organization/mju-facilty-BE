package com.example.rentalSystem.domain.suggestion.controller;

import com.example.rentalSystem.domain.suggestion.dto.request.CreateSuggestionRequestDto;
import com.example.rentalSystem.domain.suggestion.dto.request.SearchSuggestionRequestDTO;
import com.example.rentalSystem.domain.suggestion.dto.request.UpdateAnswerRequestDto;
import com.example.rentalSystem.domain.suggestion.dto.response.SuggestionResponse;
import com.example.rentalSystem.domain.suggestion.dto.response.SuggestionStatisticsResponse;
import com.example.rentalSystem.domain.suggestion.entity.SuggestionStatus;
import com.example.rentalSystem.global.auth.security.CustomerDetails;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.example.ApiErrorCodeExample;
import com.example.rentalSystem.global.response.type.ErrorType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Tag(name = "건의함 관련 API", description = "건의 등록, 수정, 조회, 답변 등")
public interface SuggestionControllerDocs {

    @Operation(summary = "건의 등록 API (학생 전용)")
    ApiResponse<?> createSuggestion(CreateSuggestionRequestDto requestDto,
                                    @Parameter(hidden = true) CustomerDetails customerDetails);

    @Operation(summary = "내 건의 수정 API (학생 전용)")
    @ApiErrorCodeExample(ErrorType.ENTITY_NOT_FOUND)
    ApiResponse<?> updateSuggestion(Long suggestionId,
                                    CreateSuggestionRequestDto requestDto,
                                    @Parameter(hidden = true) CustomerDetails customerDetails);

    @Operation(summary = "내 건의 삭제 API (학생 전용)")
    ApiResponse<?> deleteSuggestion(Long suggestionId,
                                    @Parameter(hidden = true) CustomerDetails customerDetails);

    @Operation(summary = "전체 건의 목록 조회 (비로그인 허용, 필터링 지원)")
    ApiResponse<List<SuggestionResponse>> getSuggestions(
            SearchSuggestionRequestDTO requestDTO,
            Pageable pageable
    );

    @Operation(summary = "내가 작성한 건의 목록 조회 (학생 전용)")
    ApiResponse<List<SuggestionResponse>> getMySuggestions(
            @Parameter(hidden = true) CustomerDetails customerDetails);

    @Operation(summary = "건의 답변 등록 (관리자 전용, 상태 자동 완료)")
    @ApiErrorCodeExample(ErrorType.ENTITY_NOT_FOUND)
    ApiResponse<?> answerSuggestion(Long suggestionId, UpdateAnswerRequestDto requestDto);

    @Operation(summary = "건의 상태만 변경 (관리자 전용)")
    ApiResponse<?> updateStatus(Long suggestionId, SuggestionStatus status);

    @Operation(summary = "건의 통계 조회 (관리자 전용)")
    ApiResponse<SuggestionStatisticsResponse> getStatistics();
}
