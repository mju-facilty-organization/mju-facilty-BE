package com.example.rentalSystem.domain.suggestion.controller;

import com.example.rentalSystem.domain.suggestion.dto.request.CreateSuggestionRequestDto;
import com.example.rentalSystem.domain.suggestion.dto.request.SearchSuggestionRequestDTO;
import com.example.rentalSystem.domain.suggestion.dto.request.UpdateAnswerRequestDto;
import com.example.rentalSystem.domain.suggestion.dto.response.SuggestionResponse;
import com.example.rentalSystem.domain.suggestion.dto.response.SuggestionStatisticsResponse;
import com.example.rentalSystem.domain.suggestion.entity.SuggestionStatus;
import com.example.rentalSystem.domain.suggestion.service.SuggestionService;
import com.example.rentalSystem.global.auth.security.CustomerDetails;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.type.SuccessType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/suggestions")
public class SuggestionController implements SuggestionControllerDocs {

    private final SuggestionService suggestionService;

    @GetMapping
    public ApiResponse<List<SuggestionResponse>> getSuggestions(
            @ModelAttribute SearchSuggestionRequestDTO requestDTO,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        return ApiResponse.success(
                SuccessType.SUCCESS,
                suggestionService.getSuggestions(requestDTO, null, pageable)
        );
    }

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

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping
    public ApiResponse<?> createSuggestion(
            @RequestBody @Valid CreateSuggestionRequestDto requestDto,
            @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        suggestionService.create(requestDto, customerDetails.getStudent());
        return ApiResponse.success(SuccessType.CREATED);
    }

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

    @PreAuthorize("hasRole('STUDENT')")
    @DeleteMapping("/{suggestionId}")
    public ApiResponse<?> deleteSuggestion(
            @PathVariable Long suggestionId,
            @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        suggestionService.delete(suggestionId, customerDetails.getStudent());
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PIC')")
    @PostMapping("/{suggestionId}/answer")
    public ApiResponse<?> createAnswer(
            @PathVariable Long suggestionId,
            @RequestBody @Valid UpdateAnswerRequestDto requestDto
    ) {
        suggestionService.createAnswer(suggestionId, requestDto);
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PIC')")
    @PatchMapping("/{suggestionId}/answer")
    public ApiResponse<?> updateAnswer(
            @PathVariable Long suggestionId,
            @RequestBody @Valid UpdateAnswerRequestDto requestDto
    ) {
        suggestionService.updateAnswer(suggestionId, requestDto);
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PIC')")
    @PatchMapping("/{suggestionId}/status")
    public ApiResponse<?> updateStatus(
            @PathVariable Long suggestionId,
            @RequestParam SuggestionStatus status
    ) {
        suggestionService.updateStatus(suggestionId, status);
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PIC')")
    @GetMapping("/statistics")
    public ApiResponse<SuggestionStatisticsResponse> getStatistics() {
        return ApiResponse.success(
                SuccessType.SUCCESS,
                suggestionService.getSuggestionStatistics()
        );
    }
}
