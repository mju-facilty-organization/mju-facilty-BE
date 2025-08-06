package com.example.rentalSystem.domain.book.rentalhistory.entity.type;

import static com.example.rentalSystem.domain.book.timetable.TimeStatus.AVAILABLE;
import static com.example.rentalSystem.domain.book.timetable.TimeStatus.RESERVED;

import com.example.rentalSystem.domain.book.timetable.TimeStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RentalApplicationResult {

    PIC_PERMITTED("교직원 승인 허가", RESERVED),
    PROFESSOR_PERMITTED("교수 승인 허가", TimeStatus.WAITING),
    WAITING("대기중", TimeStatus.WAITING),
    PIC_DENIED("교직원 승인 거절", AVAILABLE),
    PROFESSOR_DENIED("교수 승인 거절", AVAILABLE);

    private final String description;
    private final TimeStatus timeStatus;
}
