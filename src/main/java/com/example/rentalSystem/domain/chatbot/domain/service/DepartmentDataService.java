package com.example.rentalSystem.domain.chatbot.domain.service;

import com.example.rentalSystem.domain.chatbot.domain.type.QueryCategory;
import com.example.rentalSystem.domain.chatbot.dto.response.ExtractResponse;
import com.example.rentalSystem.domain.chatbot.service.QueryDataService;
import com.example.rentalSystem.domain.facility.dto.FacilityDto;
import com.example.rentalSystem.domain.facility.implement.FacilityReader;
import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import com.example.rentalSystem.global.util.AIUtils;
import com.example.rentalSystem.infrastructure.port.openai.AiTextProcessorPort;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentDataService implements QueryDataService {

    private final AiTextProcessorPort aiTextProcessorPort;
    private final QueryCategory queryCategory = QueryCategory.DEPARTMENTAL_FACILITY;

    private final FacilityReader facilityReader;

    // 과별 강의실에 대한 질문
    @Override
    public String getDataForQuery(String question) {
        ExtractResponse extractResponse = aiTextProcessorPort.extractItem(question, queryCategory.getExtractPrompt());
        String department = extractResponse.department();

        Optional<AffiliationType> byKeyword = AffiliationType.findByKeyword(department);

        if (byKeyword.isEmpty()) {
            return null;
        }

        List<FacilityDto> facilityListBy = facilityReader.getFacilityListBy(byKeyword.get());

        return AIUtils.createFacilityDataList(facilityListBy);
    }

    @Override
    public QueryCategory getQueryCategory() {
        return queryCategory;
    }
}
