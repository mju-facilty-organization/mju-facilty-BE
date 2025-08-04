package com.example.rentalSystem.domain.suggestion.dto.request;

import com.example.rentalSystem.domain.suggestion.entity.SuggestionCategory;
import com.example.rentalSystem.domain.suggestion.entity.SuggestionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchSuggestionRequestDTO {

    private SuggestionCategory category;
    private SuggestionStatus status;
    private String keyword;
}
