package com.example.rentalSystem.domain.suggestion.service;

import com.example.rentalSystem.domain.member.student.entity.Student;
import com.example.rentalSystem.domain.suggestion.dto.request.CreateSuggestionRequestDto;
import com.example.rentalSystem.domain.suggestion.dto.request.SearchSuggestionRequestDTO;
import com.example.rentalSystem.domain.suggestion.dto.request.UpdateAnswerRequestDto;
import com.example.rentalSystem.domain.suggestion.dto.response.SuggestionResponse;
import com.example.rentalSystem.domain.suggestion.dto.response.SuggestionStatisticsResponse;
import com.example.rentalSystem.domain.suggestion.entity.SuggestionStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SuggestionService {

    void create(CreateSuggestionRequestDto requestDto, Student student);

    void delete(Long suggestionId, Student student);

    void update(Long suggestionId, CreateSuggestionRequestDto requestDto, Student student);

    SuggestionStatisticsResponse getSuggestionStatistics();

    List<SuggestionResponse> getSuggestions(SearchSuggestionRequestDTO request, Student loginUser, Pageable pageable);

    List<SuggestionResponse> getMySuggestions(Student student);

    void answer(Long suggestionId, UpdateAnswerRequestDto dto);

    void updateStatus(Long suggestionId, SuggestionStatus status);
}