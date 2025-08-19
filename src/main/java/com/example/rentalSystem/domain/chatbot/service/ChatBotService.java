package com.example.rentalSystem.domain.chatbot.service;


import com.example.rentalSystem.domain.chatbot.domain.type.QueryCategory;
import com.example.rentalSystem.global.auth.security.CustomerDetails;
import com.example.rentalSystem.infrastructure.adapter.openai.dto.ChatBotRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatBotService {

    private final QueryServiceFactory queryServiceFactory;

    public String getChatbotResponse(ChatBotRequest chatBotRequest, CustomerDetails customerDetails) {
        QueryCategory queryCategory = chatBotRequest.queryCategory();
        ChatBotDomainService chatBotDomainService = queryServiceFactory.getDataService(queryCategory);
        return chatBotDomainService.getChatBotResponse(chatBotRequest.question(), customerDetails);
    }

}