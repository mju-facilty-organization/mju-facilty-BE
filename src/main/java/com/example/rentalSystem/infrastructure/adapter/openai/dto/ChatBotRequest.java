package com.example.rentalSystem.infrastructure.adapter.openai.dto;

import com.example.rentalSystem.domain.chatbot.domain.type.QueryCategory;
import io.swagger.v3.oas.annotations.media.Schema;

public record ChatBotRequest(
    @Schema(description = "질문의 종류", example = "DEPARTMENTAL_FACILITY,AVAILABLE_FACILITY,GENERAL_FACILITY,SPECIAL_PURPOSE_ROOM")
    QueryCategory queryCategory,
    String question
) {

}
