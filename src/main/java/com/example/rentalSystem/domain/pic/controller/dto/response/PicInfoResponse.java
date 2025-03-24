package com.example.rentalSystem.domain.pic.controller.dto.response;

import com.example.rentalSystem.domain.affiliation.type.AffiliationType;
import com.example.rentalSystem.domain.pic.entity.Pic;

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
