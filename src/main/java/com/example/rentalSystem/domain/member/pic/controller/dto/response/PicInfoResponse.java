package com.example.rentalSystem.domain.member.pic.controller.dto.response;

import com.example.rentalSystem.domain.member.affiliation.type.AffiliationType;
import com.example.rentalSystem.domain.member.pic.entity.Pic;

public record PicInfoResponse(
    String name,
    AffiliationType affiliationType
) {

    public static PicInfoResponse from(Pic pic) {
        return new PicInfoResponse(
            pic.getName(),
            pic.getCollege()
        );
    }
}
