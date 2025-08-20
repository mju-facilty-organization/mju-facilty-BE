package com.example.rentalSystem.domain.chatbot.domain.type;

import static com.example.rentalSystem.domain.chatbot.domain.type.PromptConstant.DEPARTMENTAL_ASK_PROMPT;
import static com.example.rentalSystem.domain.chatbot.domain.type.PromptConstant.DEPARTMENTAL_EXTRACT_PROMPT;
import static com.example.rentalSystem.domain.chatbot.domain.type.PromptConstant.USER_ASK_PROMPT;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QueryCategory {
    // 과별 강의실 (전공 강의실)
    DEPARTMENTAL_FACILITY("학부/과별 시설", DEPARTMENTAL_EXTRACT_PROMPT, DEPARTMENTAL_ASK_PROMPT),

    // 사용자가 소속된 과에 따라 필터링된 강의실
    USER_FACILITY("유저와 관련된 시설 질문", null, USER_ASK_PROMPT),

    // 모든 사용자가 이용할 수 있는 공용 강의실
    GENERAL_FACILITY("공공 시설", null, null),

    // 특별한 장비가 있는 강의실
    SPECIAL_PURPOSE_ROOM("특별 강의실", null, null);

    private final String description;
    private final String extractPrompt;
    private final String askPrompt;

}
