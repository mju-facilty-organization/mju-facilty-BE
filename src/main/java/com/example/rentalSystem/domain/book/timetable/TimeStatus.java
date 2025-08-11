package com.example.rentalSystem.domain.book.timetable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum TimeStatus {
    AVAILABLE("예약가능"),
    UNAVAILABLE("이용불가"),
    RESERVED("예약완료"),
    WAITING("예약대기");
    final String description;

}
