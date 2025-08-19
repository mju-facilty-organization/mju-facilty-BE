package com.example.rentalSystem.domain.chatbot.service;

import com.example.rentalSystem.domain.chatbot.domain.type.QueryCategory;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class QueryServiceFactory {

    private final Map<QueryCategory, ChatBotDomainService> services;

    public QueryServiceFactory(List<ChatBotDomainService> services) {
        this.services = new EnumMap<>(QueryCategory.class);
        this.services.putAll(
            services.stream()
                .collect(Collectors.toMap(ChatBotDomainService::getQueryCategory, Function.identity()))
        );
    }

    public ChatBotDomainService getDataService(QueryCategory queryCategory) {
        return services.get(queryCategory);
    }

}
