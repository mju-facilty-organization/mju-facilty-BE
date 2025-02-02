package com.example.rentalSystem.domain.rentalhistory.dto.response;

import com.example.rentalSystem.domain.student.dto.response.StudentResponse;

public record RentalHistoryDetailResponseDto(
    StudentResponse studentResponse,
    RentalHistoryResponseDto rentalHistoryResponseDto
//    PicResponse picResponse
) {

}
