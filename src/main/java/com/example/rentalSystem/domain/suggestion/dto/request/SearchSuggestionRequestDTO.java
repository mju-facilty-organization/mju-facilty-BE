package com.example.rentalSystem.domain.suggestion.dto.request;

import com.example.rentalSystem.domain.suggestion.entity.SuggestionCategory;
import com.example.rentalSystem.domain.suggestion.entity.SuggestionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchSuggestionRequestDTO {

    @Schema(description = "분야", example = "DIRTY", allowableValues = {"NONE", "DIRTY", "REQUEST", "BROKEN", "ETC"})
    private SuggestionCategory category;

    @Schema(description = "처리 상태", example = "RECEIVED", allowableValues = {"RECEIVED", "IN_REVIEW", "COMPLETED"})
    private SuggestionStatus status;

    private String keyword;
}
