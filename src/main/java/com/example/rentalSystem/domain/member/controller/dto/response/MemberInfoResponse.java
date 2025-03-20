package com.example.rentalSystem.domain.member.controller.dto.response;

import lombok.Builder;

@Builder
public record MemberInfoResponse(
    long id,
    String name
) {

}
