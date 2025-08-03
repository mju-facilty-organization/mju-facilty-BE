package com.example.rentalSystem.domain.suggestion.dto.response;

import com.example.rentalSystem.domain.suggestion.entity.Suggestion;
import com.example.rentalSystem.domain.suggestion.util.NameMaskingUtil;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SuggestionResponse {

    private Long id;
    private String title;
    private String content;

    private String category;
    private String categoryCode;

    private String status;
    private String statusCode;

    private String maskedStudentName;
    private String facilityNumber;

    private String answer;
    private LocalDateTime answeredAt;

    private LocalDateTime createdAt;

    public static SuggestionResponse from(Suggestion suggestion, boolean canViewAnswer) {
        return SuggestionResponse.builder()
                .id(suggestion.getId())
                .title(suggestion.getTitle())
                .content(suggestion.getContent())

                .category(suggestion.getCategory().getLabel())
                .categoryCode(suggestion.getCategory().name())

                .status(suggestion.getStatus().getLabel())
                .statusCode(suggestion.getStatus().name())

                .maskedStudentName(
                        suggestion.getStudent() != null && suggestion.getStudent().getName() != null
                                ? NameMaskingUtil.mask(suggestion.getStudent().getName())
                                : "익명"
                )

                .facilityNumber(
                        suggestion.getFacility() != null
                                ? suggestion.getFacility().getFacilityNumber()
                                : "미지정"
                )

                .answer(canViewAnswer ? suggestion.getAdminAnswer() : null)
                .answeredAt(canViewAnswer ? suggestion.getAnsweredAt() : null)

                .createdAt(suggestion.getCreatedAt())
                .build();
    }
}