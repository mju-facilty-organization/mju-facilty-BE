package com.example.rentalSystem.global.util;

import com.example.rentalSystem.domain.chatbot.domain.type.QueryCategory;
import com.example.rentalSystem.domain.facility.dto.FacilityDto;
import java.util.List;
import java.util.stream.Collectors;

public class RequestUtils {

    public static String merge(String question, String extractPrompt) {
        return question + System.lineSeparator() + extractPrompt;
    }

    public static String createFacilityDataList(List<FacilityDto> facilityListBy) {
        return facilityListBy.stream()
            .map(facility -> String.format(
                "시설 타입: %s, 시설 번호: %s, 수용 인원: %d명",
                facility.facilityType(),
                facility.facilityNumber(),
                facility.capacity()
            ))
            .collect(Collectors.joining("\n"));
    }

    public static String merge(String dataForQuery, QueryCategory queryCategory, String question) {
        return queryCategory.getAskPrompt().formatted(dataForQuery, queryCategory);

    }
}
