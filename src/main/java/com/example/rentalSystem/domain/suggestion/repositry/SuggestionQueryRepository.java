package com.example.rentalSystem.domain.suggestion.repositry;

import com.example.rentalSystem.domain.member.student.entity.Student;
import com.example.rentalSystem.domain.suggestion.dto.request.SearchSuggestionRequestDTO;
import com.example.rentalSystem.domain.suggestion.dto.response.SuggestionResponse;

import java.util.List;

public interface SuggestionQueryRepository {
    List<SuggestionResponse> searchSuggestions(SearchSuggestionRequestDTO request, Student loginUser, int size);
}