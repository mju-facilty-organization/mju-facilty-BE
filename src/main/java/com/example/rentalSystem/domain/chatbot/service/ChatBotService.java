package com.example.rentalSystem.domain.chatbot.service;


import static com.example.rentalSystem.domain.chatbot.domain.type.FixResponseConstant.NOT_FOUND_AFFILIATION;

import com.example.rentalSystem.domain.chatbot.domain.type.QueryCategory;
import com.example.rentalSystem.infrastructure.adapter.openai.dto.ChatBotRequest;
import com.example.rentalSystem.infrastructure.port.openai.AiTextProcessorPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatBotService {

    private final AiTextProcessorPort aiTextProcessorPort;
    private final QueryServiceFactory queryServiceFactory;

    public String getChatbotResponse(ChatBotRequest chatBotRequest) {
        QueryCategory queryCategory = chatBotRequest.queryCategory();
        QueryDataService dataService = queryServiceFactory.getDataService(queryCategory);
        String request = dataService.getDataForQuery(chatBotRequest.question());
        if (request == null) {
            return NOT_FOUND_AFFILIATION;
        }
        return aiTextProcessorPort.ask(request);
    }
}