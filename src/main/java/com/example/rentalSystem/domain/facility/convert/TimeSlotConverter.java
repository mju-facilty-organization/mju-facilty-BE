package com.example.rentalSystem.domain.facility.convert;

import com.example.rentalSystem.domain.facility.entity.RentalStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import java.time.LocalTime;
import java.util.LinkedHashMap;

public class TimeSlotConverter implements
    AttributeConverter<LinkedHashMap<LocalTime, RentalStatus>, String> {

    private final ObjectMapper mapper;

    public TimeSlotConverter() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());  // LocalTime 처리용 JavaTimeModule 등록
    }

    @Override
    public String convertToDatabaseColumn(LinkedHashMap<LocalTime, RentalStatus> timeSlot) {
        try {
            return mapper.writeValueAsString(timeSlot);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing TimeSlot", e);
        }
    }

    @Override
    public LinkedHashMap<LocalTime, RentalStatus> convertToEntityAttribute(String data) {
        try {
            return mapper.readValue(data,
                mapper.getTypeFactory()
                    .constructMapType(LinkedHashMap.class, LocalTime.class, RentalStatus.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing TimeSlot", e);
        }
    }
}
