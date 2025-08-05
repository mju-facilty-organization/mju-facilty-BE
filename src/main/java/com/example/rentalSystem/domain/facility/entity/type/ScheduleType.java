package com.example.rentalSystem.domain.facility.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleType {
    CLASS("수업"),
    CLUB("동아리"),
    SEMINAR("세미나"),
    MEETING("회의"),
    OTHER("기타");

    private final String description;
}
