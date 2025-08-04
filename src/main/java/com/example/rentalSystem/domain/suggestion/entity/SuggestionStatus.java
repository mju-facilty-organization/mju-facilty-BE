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

    @JsonCreator
    public static SuggestionStatus from(String input) {
        for (SuggestionStatus status : SuggestionStatus.values()) {
            if (status.name().equalsIgnoreCase(input) || status.label.equalsIgnoreCase(input)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + input);
    }
}
