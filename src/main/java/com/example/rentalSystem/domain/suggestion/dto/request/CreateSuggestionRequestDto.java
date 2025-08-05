package com.example.rentalSystem.domain.suggestion.dto.request;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.suggestion.entity.SuggestionCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateSuggestionRequestDto {

    @NotNull(message = "건의하실 분야를 선택해주세요.")
    private SuggestionCategory category;

    @NotNull(message = "건의 내용이 있는 강의실을 선택해주세요.")
    private Long facilityId;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
