package com.example.rentalSystem.domain.chatbot.controller;

import com.example.rentalSystem.global.auth.security.CustomerDetails;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.infrastructure.adapter.openai.dto.ChatBotRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "챗봇", description = "챗봇과 주고받는 대화용 api")
public interface ChatBotControllerDocs {

    @PostMapping("/ask")
    @Operation(description = "chat bot 요청")
    ApiResponse<?> ask(ChatBotRequest chatBotRequest, CustomerDetails customerDetails);
}
