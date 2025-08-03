package com.example.rentalSystem.domain.suggestion.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum SuggestionStatus {
    RECEIVED("접수됨"),
    IN_REVIEW("처리중"),
    COMPLETED("처리완료");

    private final String label;

    SuggestionStatus(String label) {
        this.label = label;
    }
}
