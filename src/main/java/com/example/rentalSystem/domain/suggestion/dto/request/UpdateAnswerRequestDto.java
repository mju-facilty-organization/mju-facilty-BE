package com.example.rentalSystem.domain.suggestion.dto.request;

import com.example.rentalSystem.domain.suggestion.entity.SuggestionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateAnswerRequestDto {

    @NotBlank(message = "답변 내용을 입력해주세요.")
    private String answer;

    private SuggestionStatus status;
}