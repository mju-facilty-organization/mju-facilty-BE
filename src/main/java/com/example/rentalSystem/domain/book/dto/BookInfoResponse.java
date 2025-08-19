package com.example.rentalSystem.domain.book.dto;

import com.example.rentalSystem.domain.book.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.book.schedule.entity.Schedule;
import lombok.Builder;

@Builder
public record BookInfoResponse(
    String bookName,
    String type
) {

    public static BookInfoResponse fromClass(RentalHistory rh) {
        return BookInfoResponse.builder()
            .bookName(rh.getStudent().getName() + "학생 시설 대여 중")
            .type(rh.getPurpose())
            .build();
    }

    public static BookInfoResponse fromClass(Schedule sh) {
        return BookInfoResponse.builder()
            .bookName(sh.getScheduleName())
            .type(sh.getScheduleType().getDescription())
            .build();
    }

    public static BookInfoResponse createEmpty() {
        return BookInfoResponse.builder()
            .bookName("없음")
            .type("없음")
            .build();
    }

    public static BookInfoResponse from(Schedule schedule) {
        return BookInfoResponse.builder()
            .bookName(schedule.getOrganization())
            .type(schedule.getScheduleType().getDescription())
            .build();
    }
}
