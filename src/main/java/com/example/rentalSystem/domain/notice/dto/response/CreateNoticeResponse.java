package com.example.rentalSystem.domain.notice.dto.response;

import lombok.Builder;

@Builder
public record CreateNoticeResponse(
    Long id,
    String presignedUrlForPut
) {

}
