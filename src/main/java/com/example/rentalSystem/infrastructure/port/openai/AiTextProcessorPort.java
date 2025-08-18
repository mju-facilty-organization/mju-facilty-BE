package com.example.rentalSystem.infrastructure.port.openai;

import com.example.rentalSystem.domain.chatbot.dto.response.ExtractResponse;

public interface AiTextProcessorPort {

    String ask(String userMessage);

    ExtractResponse extractItem(String question, String extractPrompt);
}
