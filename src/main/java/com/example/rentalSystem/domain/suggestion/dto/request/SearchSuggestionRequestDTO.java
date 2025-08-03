package com.example.rentalSystem.domain.suggestion.dto.request;

import com.example.rentalSystem.domain.suggestion.entity.SuggestionCategory;
import com.example.rentalSystem.domain.suggestion.entity.SuggestionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchSuggestionRequestDTO {

    private SuggestionCategory category;    // 분야 (nullable)
    private SuggestionStatus status;        // 상태 (nullable)
    private String keyword;                 // 제목/내용 검색 (nullable)
    private Integer page = 0;
}