package com.example.rentalSystem.domain.common.convert;

import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import java.util.List;

public class AffiliationListConverter implements AttributeConverter<List<AffiliationType>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<AffiliationType> affiliationTypes) {
        try {
            return mapper.writeValueAsString(affiliationTypes);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AffiliationType> convertToEntityAttribute(String s) {
        if (s == null || s.isBlank()) {
            return List.of();
        }

        try {
            return mapper.readValue(s, new TypeReference<List<AffiliationType>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 파싱 실패: " + s, e);
        }
    }
}
