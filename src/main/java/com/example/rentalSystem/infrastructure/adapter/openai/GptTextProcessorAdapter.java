package com.example.rentalSystem.infrastructure.adapter.openai;

import com.example.rentalSystem.domain.chatbot.dto.response.ExtractResponse;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import com.example.rentalSystem.global.util.RequestUtils;
import com.example.rentalSystem.infrastructure.adapter.openai.dto.ChatGptRequest;
import com.example.rentalSystem.infrastructure.adapter.openai.dto.ChatGptResponse;
import com.example.rentalSystem.infrastructure.adapter.openai.dto.Message;
import com.example.rentalSystem.infrastructure.port.openai.AiTextProcessorPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class GptTextProcessorAdapter implements AiTextProcessorPort {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Override
    public String ask(String userMessage) {

        Message message = createUserMessage(userMessage);

        ChatGptRequest request = createRequest(message);

        long startTime = System.nanoTime();

        ChatGptResponse chatGptResponse = webClient.post()
            .uri("/chat/completions")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .bodyToMono(ChatGptResponse.class)
            .block();
        long endTime = System.nanoTime();
        long durationMillis = (endTime - startTime) / 1_000_000;
        log.info("GPT API 호출 시간: {}ms", durationMillis);

        if (chatGptResponse == null || chatGptResponse.getChoices().isEmpty()) {
            return "GPT 응답을 가져오지 못했습니다.";
        }

        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }

    @Override
    public ExtractResponse extractItem(String question, String extractPrompt) {
        Message message = createSystemMessage(RequestUtils.merge(question, extractPrompt));
        ChatGptRequest request = createRequest(message);

        ChatGptResponse chatGptResponse = webClient.post()
            .uri("/chat/completions")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .bodyToMono(ChatGptResponse.class)
            .block();

        if (chatGptResponse == null || chatGptResponse.getChoices().isEmpty()) {
            return null;
        }

        // 3. Extract the nested JSON string from the content field
        String jsonContent = chatGptResponse.getChoices().get(0).getMessage().getContent();

        ExtractResponse response;

        try {
            response = objectMapper.readValue(jsonContent, ExtractResponse.class);
        } catch (IOException e) {
            throw new CustomException(ErrorType.FAIL_JSON_PARSING);
        }
        response = response.fillDateAndTime();
        return response;
    }


    private static ChatGptRequest createRequest(Message message) {
        return ChatGptRequest.builder()
            .model("gpt-4o-mini")
            .messages(List.of(message))
            .temperature(0.0)
            .max_tokens(300)
            .frequency_penalty(0.0) // temperature: 모델이 응답을 생성할 때의 창의성 또는 무작위성을 조절합니다.
            .build();
    }

    private static Message createUserMessage(String userMessage) {
        return Message.builder()
            .role("user")
            .content(userMessage)
            .build();
    }

    private Message createSystemMessage(String systemMessage) {
        return Message.builder()
            .role("system")
            .content(systemMessage)
            .build();
    }


}
