package com.example.rentalSystem.domain.chatbot.service;

import com.example.rentalSystem.domain.chatbot.domain.type.QueryCategory;

public interface QueryDataService {

    String getDataForQuery(String question);

    QueryCategory getQueryCategory();
}
