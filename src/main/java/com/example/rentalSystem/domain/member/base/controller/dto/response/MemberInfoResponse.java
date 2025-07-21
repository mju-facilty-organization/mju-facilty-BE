package com.example.rentalSystem.domain.member.base.controller.dto.response;

import lombok.Builder;

@Builder
public record MemberInfoResponse(
    long id,
    String name
) {

}
