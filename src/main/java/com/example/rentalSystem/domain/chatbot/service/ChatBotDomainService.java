package com.example.rentalSystem.domain.chatbot.service;

import com.example.rentalSystem.domain.chatbot.domain.type.QueryCategory;
import com.example.rentalSystem.global.auth.security.CustomerDetails;

public interface ChatBotDomainService {

    String getChatBotResponse(String question, CustomerDetails customerDetailsOptional);

    QueryCategory getQueryCategory();
}
