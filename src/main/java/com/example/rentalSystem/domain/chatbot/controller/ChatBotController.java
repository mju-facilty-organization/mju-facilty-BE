package com.example.rentalSystem.domain.chatbot.controller;

import com.example.rentalSystem.domain.chatbot.service.ChatBotService;
import com.example.rentalSystem.global.auth.security.CustomerDetails;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.type.SuccessType;
import com.example.rentalSystem.infrastructure.adapter.openai.dto.ChatBotRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatbot")
public class ChatBotController implements ChatBotControllerDocs {

    private final ChatBotService chatbotService;

    @Override
    @PostMapping("/ask")
    public ApiResponse<?> ask(
        @RequestBody ChatBotRequest chatBotRequest,
        @AuthenticationPrincipal CustomerDetails customerDetailsOptional
    ) {
        String chatbotResponse = chatbotService.getChatbotResponse(chatBotRequest, customerDetailsOptional);
        return ApiResponse.success(SuccessType.SUCCESS, chatbotResponse);
    }
}
