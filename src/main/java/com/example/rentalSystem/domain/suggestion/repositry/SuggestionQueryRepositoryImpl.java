package com.example.rentalSystem.domain.suggestion.repositry;

import com.example.rentalSystem.domain.member.student.entity.Student;
import com.example.rentalSystem.domain.suggestion.dto.request.SearchSuggestionRequestDTO;
import com.example.rentalSystem.domain.suggestion.dto.response.SuggestionResponse;
import com.example.rentalSystem.domain.suggestion.entity.QSuggestion;
import com.example.rentalSystem.domain.suggestion.entity.Suggestion;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SuggestionQueryRepositoryImpl implements SuggestionQueryRepository {

    private final JPAQueryFactory queryFactory;
    private static final QSuggestion qSuggestion = QSuggestion.suggestion;

    private static final int DEFAULT_PAGE_SIZE = 5;
    private static final int MAX_PAGE_SIZE = 20;

    @Override
    public List<SuggestionResponse> searchSuggestions(SearchSuggestionRequestDTO request, Student loginUser, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (request.getStatus() != null) {
            builder.and(qSuggestion.status.eq(request.getStatus()));
        }
        if (request.getCategory() != null) {
            builder.and(qSuggestion.category.eq(request.getCategory()));
        }

        if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
            String[] keywords = request.getKeyword().trim().split("\\s+");
            BooleanBuilder keywordBuilder = new BooleanBuilder();

            for (String word : keywords) {
                String pattern = "%" + word.toLowerCase() + "%";

                keywordBuilder.and(
                        Expressions.stringTemplate("lower(cast({0} as string))", qSuggestion.title).like(pattern)
                                .or(Expressions.stringTemplate("lower(cast({0} as string))", qSuggestion.content).like(pattern))
                );
            }

            builder.and(keywordBuilder);
        }

        int requestedSize = pageable.getPageSize() > 0 ? pageable.getPageSize() : DEFAULT_PAGE_SIZE;
        int safeSize = Math.min(requestedSize, MAX_PAGE_SIZE);

        Pageable safePageable = PageRequest.of(pageable.getPageNumber(), safeSize, pageable.getSort());

        List<Suggestion> suggestions = queryFactory
                .selectFrom(qSuggestion)
                .where(builder)
                .orderBy(qSuggestion.createdAt.desc())
                .offset(safePageable.getOffset())
                .limit(safePageable.getPageSize())
                .fetch();

        return suggestions.stream()
                .map(s -> SuggestionResponse.from(s, true))
                .toList();
    }
}