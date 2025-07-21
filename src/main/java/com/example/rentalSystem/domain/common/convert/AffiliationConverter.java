package com.example.rentalSystem.domain.common.convert;

import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import jakarta.persistence.AttributeConverter;

public class AffiliationConverter implements AttributeConverter<AffiliationType, String> {

    @Override
    public String convertToDatabaseColumn(AffiliationType affiliationType) {
        if (affiliationType == null) {
            return null;
        }
        return affiliationType.getName();
    }

    @Override
    public AffiliationType convertToEntityAttribute(String name) {
        return AffiliationType.getInstance(name);
    }
}
