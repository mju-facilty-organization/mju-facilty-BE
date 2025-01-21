package com.example.rentalSystem.domain.facility.convert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Converter(autoApply = true)
public class WeekScheduleListConverter implements
    AttributeConverter<Map<String, List<String>>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, List<String>> weekSchedules) {
        try {
            // List<WeekSchedule>을 JSON 문자열로 변환
            return objectMapper.writeValueAsString(weekSchedules);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting List<WeekSchedule> to String", e);
        }
    }

    @Override
    public Map<String, List<String>> convertToEntityAttribute(String dbData) {
        try {
            // JSON 문자열을  Map<String, List<String>>로 변환
            return objectMapper.readValue(dbData, new TypeReference<Map<String, List<String>>>() {
            });
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting String to List<WeekSchedule>", e);
        }
    }
}
