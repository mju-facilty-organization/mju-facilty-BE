package com.example.rentalSystem.domain.suggestion.implement;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.reposiotry.FacilityJpaRepository;
import com.example.rentalSystem.domain.member.student.entity.Student;
import com.example.rentalSystem.domain.suggestion.dto.request.CreateSuggestionRequestDto;
import com.example.rentalSystem.domain.suggestion.dto.request.SearchSuggestionRequestDTO;
import com.example.rentalSystem.domain.suggestion.dto.request.UpdateAnswerRequestDto;
import com.example.rentalSystem.domain.suggestion.dto.response.SuggestionResponse;
import com.example.rentalSystem.domain.suggestion.dto.response.SuggestionStatisticsResponse;
import com.example.rentalSystem.domain.suggestion.entity.Suggestion;
import com.example.rentalSystem.domain.suggestion.entity.SuggestionStatus;
import com.example.rentalSystem.domain.suggestion.repositry.SuggestionJpaRepository;
import com.example.rentalSystem.domain.suggestion.repositry.SuggestionQueryRepository;
import com.example.rentalSystem.domain.suggestion.service.SuggestionService;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SuggestionServiceImpl implements SuggestionService {

    private final SuggestionJpaRepository suggestionRepository;
    private final FacilityJpaRepository facilityRepository;
    private final SuggestionQueryRepository suggestionQueryRepository;

    private static final int SUGGESTIONS_PER_PAGE = 5;

    @Override
    @Transactional
    public void create(CreateSuggestionRequestDto requestDto, Student student) {
        Facility facility = facilityRepository.findById(requestDto.getFacilityId())
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));

        Suggestion suggestion = Suggestion.builder()
                .category(requestDto.getCategory())
                .facility(facility)
                .student(student)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

        suggestionRepository.save(suggestion);
    }

    @Override
    @Transactional
    public void delete(Long suggestionId, Student student) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));

        if (!suggestion.getStudent().getId().equals(student.getId())) {
            throw new CustomException(ErrorType.FORBIDDEN);
        }

        suggestionRepository.delete(suggestion);
    }

    @Override
    @Transactional
    public void update(Long suggestionId, CreateSuggestionRequestDto requestDto, Student student) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));

        if (!suggestion.getStudent().getId().equals(student.getId())) {
            throw new CustomException(ErrorType.FORBIDDEN);
        }

        Facility facility = facilityRepository.findById(requestDto.getFacilityId())
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));

        suggestion.updateSuggestion(
                requestDto.getCategory(),
                facility,
                requestDto.getTitle(),
                requestDto.getContent()
        );
    }

    @Override
    public List<SuggestionResponse> getSuggestions(SearchSuggestionRequestDTO request, Student loginUser) {
        return suggestionQueryRepository.searchSuggestions(request, loginUser, SUGGESTIONS_PER_PAGE);
    }

    @Override
    public List<SuggestionResponse> getMySuggestions(Student student) {
        return suggestionRepository.findByStudent(student).stream()
                .map(s -> SuggestionResponse.from(s, true))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void answer(Long suggestionId, UpdateAnswerRequestDto dto) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));

        suggestion.updateAnswer(dto.getAnswer());

        if (dto.getStatus() != null) {
            suggestion.setStatus(dto.getStatus());
        } else {
            suggestion.setStatus(SuggestionStatus.COMPLETED);
        }
    }

    @Override
    @Transactional
    public void updateStatus(Long suggestionId, SuggestionStatus status) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));

        suggestion.setStatus(status);
    }

    @Override
    public SuggestionStatisticsResponse getSuggestionStatistics() {
        List<Suggestion> all = suggestionRepository.findAll();

        long total = all.size();
        long received = all.stream().filter(s -> s.getStatus() == SuggestionStatus.RECEIVED).count();
        long reviewing = all.stream().filter(s -> s.getStatus() == SuggestionStatus.IN_REVIEW).count();
        long completed = all.stream().filter(s -> s.getStatus() == SuggestionStatus.COMPLETED).count();

        return SuggestionStatisticsResponse.builder()
                .total(total)
                .received(received)
                .reviewing(reviewing)
                .completed(completed)
                .build();
    }
}