package com.example.rentalSystem.infrastructure.adapter.openai.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatGptRequest {

    private String model;

    private List<Message> messages;

    private Double temperature;

    private Integer max_tokens;

    private Double frequency_penalty;
}
