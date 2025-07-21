package com.example.rentalSystem.domain.facility.dto.response;

import java.util.List;

public record PreSignUrlListResponse(List<String> presignedUrlList) {

    public static PreSignUrlListResponse from(List<String> presignedUrlList) {
        return new PreSignUrlListResponse(presignedUrlList);
    }
}
