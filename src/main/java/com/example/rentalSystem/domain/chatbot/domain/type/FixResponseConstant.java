package com.example.rentalSystem.domain.chatbot.domain.type;

public class FixResponseConstant {

    public static final String NOT_FOUND_AFFILIATION = """
        죄송합니다. 제공하신 학부/전공에 대한 정보를 찾을 수 없습니다. \n 
        혹시 학과 이름이 정확한지 다시 확인해 주시거나, 다른 키워드로 검색해 주시겠어요?
        """;
    public static final String LOGIN_REQUIRED_MESSAGE = """
        해당 질문은 로그인을 하셔야 응답이 가능해요!\n
        로그인이 힘드시면, 학부/과별 카테고리를 선택해서 질문을 주세요!
        """;

    public static final String FAIL_PROCESSING_MESSAGE = "죄송합니다. 일시적인 오류로 인해 요청을 처리하지 못했습니다.\n 잠시 후 다시 시도해 주세요";
}
