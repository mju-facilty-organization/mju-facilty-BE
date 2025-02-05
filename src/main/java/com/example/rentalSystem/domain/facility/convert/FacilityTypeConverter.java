package com.example.rentalSystem.domain.facility.convert;

import com.example.rentalSystem.domain.facility.entity.FacilityType;
import jakarta.persistence.AttributeConverter;

public class FacilityTypeConverter implements AttributeConverter<FacilityType, String> {

    @Override
    public String convertToDatabaseColumn(FacilityType facilityType) {
        if (facilityType == null) {
            return null;
        }
        return facilityType.getValue();
    }

    @Override
    public FacilityType convertToEntityAttribute(String value) {
        return FacilityType.getInstanceByValue(value);
    }
}
