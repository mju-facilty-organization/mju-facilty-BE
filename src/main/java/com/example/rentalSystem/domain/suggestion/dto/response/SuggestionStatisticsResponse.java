package com.example.rentalSystem.domain.suggestion.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuggestionStatisticsResponse {
    private long total;
    private long received;
    private long reviewing;
    private long completed;
}