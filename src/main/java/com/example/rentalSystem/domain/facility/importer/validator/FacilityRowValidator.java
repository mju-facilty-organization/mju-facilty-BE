package com.example.rentalSystem.domain.facility.importer.validator;

import com.example.rentalSystem.domain.facility.dto.model.FacilityRowDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FacilityRowValidator {

    public List<String> validate(FacilityRowDto row) {
        List<String> errors = new ArrayList<>();
        if (blank(row.getFacilityTypeKo())) errors.add("시설유형 누락");
        if (blank(row.getFacilityNumber())) errors.add("시설번호 누락");
        if (blank(row.getCapacityKo())) errors.add("수용인원 누락");
        if (blank(row.getStartTime())) errors.add("시작시간 누락");
        if (blank(row.getEndTime())) errors.add("종료시간 누락");
        if (blank(row.getCollegeKo())) errors.add("단과대학 누락");
        return errors;
    }

    private boolean blank(String s) {
        return s == null || s.isBlank();
    }
}