package com.example.rentalSystem.domain.suggestion.controller;

import com.example.rentalSystem.domain.member.student.entity.Student;
import com.example.rentalSystem.domain.member.student.repository.StudentRepository;
import com.example.rentalSystem.domain.suggestion.dto.request.CreateSuggestionRequestDto;
import com.example.rentalSystem.domain.suggestion.dto.request.SearchSuggestionRequestDTO;
import com.example.rentalSystem.domain.suggestion.dto.request.UpdateAnswerRequestDto;
import com.example.rentalSystem.domain.suggestion.dto.response.SuggestionResponse;
import com.example.rentalSystem.domain.suggestion.dto.response.SuggestionStatisticsResponse;
import com.example.rentalSystem.domain.suggestion.entity.SuggestionStatus;
import com.example.rentalSystem.domain.suggestion.service.SuggestionService;
import com.example.rentalSystem.global.auth.security.CustomerDetails;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.type.ErrorType;
import com.example.rentalSystem.global.response.type.SuccessType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/suggestions")
public class SuggestionController implements SuggestionControllerDocs {
    private final SuggestionService suggestionService;

    // 1. 건의 등록 - 학생만 가능
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping
    public ApiResponse<?> createSuggestion(
            @RequestBody @Valid CreateSuggestionRequestDto requestDto,
            @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        suggestionService.create(requestDto, customerDetails.getStudent());
        return ApiResponse.success(SuccessType.CREATED);
    }

    // 2. 건의 삭제 - 학생만 가능
    @PreAuthorize("hasRole('STUDENT')")
    @DeleteMapping("/{suggestionId}")
    public ApiResponse<?> deleteSuggestion(
            @PathVariable Long suggestionId,
            @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        suggestionService.delete(suggestionId, customerDetails.getStudent());
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    // 3. 내 건의 수정 - 학생만 가능
    @PreAuthorize("hasRole('STUDENT')")
    @PatchMapping("/{suggestionId}")
    public ApiResponse<?> updateSuggestion(
            @PathVariable Long suggestionId,
            @RequestBody @Valid CreateSuggestionRequestDto requestDto,
            @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        suggestionService.update(suggestionId, requestDto, customerDetails.getStudent());
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    // 4. 전체 건의 목록 조회 (비로그인도 허용)
    @GetMapping
    public ApiResponse<List<SuggestionResponse>> getSuggestions(
            @ModelAttribute SearchSuggestionRequestDTO requestDTO
    ) {
        return ApiResponse.success(
                SuccessType.SUCCESS,
                suggestionService.getSuggestions(requestDTO, null)
        );
    }

    // 5. 내가 작성한 건의 목록 - 학생만 가능
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/me")
    public ApiResponse<List<SuggestionResponse>> getMySuggestions(
            @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        return ApiResponse.success(
                SuccessType.SUCCESS,
                suggestionService.getMySuggestions(customerDetails.getStudent())
        );
    }

    // 6. 관리자 - 답변 등록 + 상태 변경
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{suggestionId}/answer")
    public ApiResponse<?> answerSuggestion(
            @PathVariable Long suggestionId,
            @RequestBody @Valid UpdateAnswerRequestDto requestDto
    ) {
        suggestionService.answer(suggestionId, requestDto);
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    // 7. 관리자 - 상태만 변경
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{suggestionId}/status")
    public ApiResponse<?> updateStatus(
            @PathVariable Long suggestionId,
            @RequestParam SuggestionStatus status
    ) {
        suggestionService.updateStatus(suggestionId, status);
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    // 8. 관리자 - 통계
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/statistics")
    public ApiResponse<SuggestionStatisticsResponse> getStatistics() {
        return ApiResponse.success(
                SuccessType.SUCCESS,
                suggestionService.getSuggestionStatistics()
        );
    }
}