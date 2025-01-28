package com.example.rentalSystem.domain.facility.convert;

import com.example.rentalSystem.domain.facility.entity.TimeSlot;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import com.fasterxml.jackson.core.type.TypeReference;

@Convert
public class TimeSlotConverter implements
    AttributeConverter<TimeSlot, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(TimeSlot timeSlot) {
        try {
            return mapper.writeValueAsString(timeSlot);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TimeSlot convertToEntityAttribute(String data) {
        try {
            return mapper.readValue(data, new TypeReference<TimeSlot>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

}
