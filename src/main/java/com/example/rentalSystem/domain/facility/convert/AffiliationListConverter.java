package com.example.rentalSystem.domain.facility.convert;

import com.example.rentalSystem.domain.affiliation.type.AffiliationType;
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
        try {
            return mapper.readValue(s, new TypeReference<List<AffiliationType>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
