package com.example.rentalSystem.domain.chatbot.domain.service;

import static com.example.rentalSystem.domain.chatbot.domain.type.FixResponseConstant.FAIL_PROCESSING_MESSAGE;
import static com.example.rentalSystem.domain.chatbot.domain.type.FixResponseConstant.NOT_FOUND_AFFILIATION;

import com.example.rentalSystem.domain.chatbot.domain.type.QueryCategory;
import com.example.rentalSystem.domain.chatbot.dto.response.ExtractResponse;
import com.example.rentalSystem.domain.chatbot.service.ChatBotDomainService;
import com.example.rentalSystem.domain.facility.dto.FacilityDto;
import com.example.rentalSystem.domain.facility.implement.FacilityReader;
import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import com.example.rentalSystem.global.auth.security.CustomerDetails;
import com.example.rentalSystem.global.util.RequestUtils;
import com.example.rentalSystem.infrastructure.port.openai.AiTextProcessorPort;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentService implements ChatBotDomainService {

    private final AiTextProcessorPort aiTextProcessorPort;
    private final QueryCategory queryCategory = QueryCategory.DEPARTMENTAL_FACILITY;

    private final FacilityReader facilityReader;

    // 과별 강의실에 대한 질문
    @Override
    public String getChatBotResponse(String question, CustomerDetails customerDetailsOptional) {
        try {
            ExtractResponse extractResponse = aiTextProcessorPort.extractItem(question,
                queryCategory.getExtractPrompt());
            String department = extractResponse.department();
            if (department == null || department.isBlank()) {
                return NOT_FOUND_AFFILIATION;
            }
            Optional<AffiliationType> byKeyword = AffiliationType.findByKeyword(department);

            if (byKeyword.isEmpty()) {
                return NOT_FOUND_AFFILIATION;
            }

            List<FacilityDto> facilityListBy = facilityReader.getFacilityListBy(byKeyword.get());
            String facilityDataList = RequestUtils.createFacilityDataList(facilityListBy);

            String finalRequest = RequestUtils.merge(facilityDataList, queryCategory, question);
            return aiTextProcessorPort.ask(finalRequest);
        } catch (Exception e) {
            return FAIL_PROCESSING_MESSAGE;
        }
    }

    @Override
    public QueryCategory getQueryCategory() {
        return queryCategory;
    }
}
