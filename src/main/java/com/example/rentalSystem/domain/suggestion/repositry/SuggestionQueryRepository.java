package com.example.rentalSystem.domain.suggestion.repositry;

import com.example.rentalSystem.domain.member.student.entity.Student;
import com.example.rentalSystem.domain.suggestion.dto.request.SearchSuggestionRequestDTO;
import com.example.rentalSystem.domain.suggestion.dto.response.SuggestionResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SuggestionQueryRepository {
    List<SuggestionResponse> searchSuggestions(SearchSuggestionRequestDTO request, Student loginUser, Pageable pageable);
}