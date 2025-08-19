package com.example.rentalSystem.domain.chatbot.domain.service;


import static com.example.rentalSystem.domain.chatbot.domain.type.QueryCategory.USER_FACILITY;

import com.example.rentalSystem.domain.chatbot.domain.type.FixResponseConstant;
import com.example.rentalSystem.domain.chatbot.domain.type.QueryCategory;
import com.example.rentalSystem.domain.chatbot.service.ChatBotCategoryService;
import com.example.rentalSystem.domain.facility.dto.FacilityDto;
import com.example.rentalSystem.domain.facility.implement.FacilityReader;
import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import com.example.rentalSystem.domain.member.student.entity.Student;
import com.example.rentalSystem.global.auth.security.CustomerDetails;
import com.example.rentalSystem.global.util.RequestUtils;
import com.example.rentalSystem.infrastructure.port.openai.AiTextProcessorPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserChatBotService implements ChatBotCategoryService {

    private final QueryCategory queryCategory = USER_FACILITY;
    private final FacilityReader facilityReader;
    private final AiTextProcessorPort aiTextProcessorPort;

    @Override
    public String getChatBotResponse(String question, CustomerDetails customerDetails) {
        if (customerDetails == null) {
            return FixResponseConstant.LOGIN_REQUIRED_MESSAGE;
        }
        Student student = customerDetails.getStudent();
        AffiliationType major = student.getMajor();
        List<FacilityDto> facilityListBy = facilityReader.getFacilityListBy(major);
        String facilityDataList = RequestUtils.createFacilityDataList(facilityListBy);
        String finalRequest = RequestUtils.merge(facilityDataList, queryCategory, question);

        return aiTextProcessorPort.ask(finalRequest);
    }

    @Override
    public QueryCategory getQueryCategory() {
        return queryCategory;
    }
}
