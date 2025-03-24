package com.example.rentalSystem.domain.rentalhistory.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Purpose {
    ACADEMIC_ACTIVITY("학과 활동"),
    CLUB_ACTIVITY("동아리 활동"),
    SPORTS_FESTIVAL_PREPARATION("체전 준비"),
    PERFORMANCE_PREPARATION("공연 준비 및 활동"),
    FESTIVAL_PREPARATION("축제 준비"),
    OTHER("기타");
    
    final String description;
}
