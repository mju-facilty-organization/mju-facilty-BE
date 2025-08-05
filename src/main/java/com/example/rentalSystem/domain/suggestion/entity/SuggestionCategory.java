package com.example.rentalSystem.domain.suggestion.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SuggestionCategory {
    NONE("없어요"),
    DIRTY("더러워요"),
    REQUEST("해주세요"),
    BROKEN("안되요"),
    ETC("기타");

    private final String label;

    SuggestionCategory(String label) {
        this.label = label;
    }

    @JsonValue
    public String getName() {
        return this.name();
    }

    @JsonCreator
    public static SuggestionCategory from(String input) {
        return Arrays.stream(SuggestionCategory.values())
                .filter(v -> v.name().equalsIgnoreCase(input) || v.label.equals(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown category: " + input));
    }
}
