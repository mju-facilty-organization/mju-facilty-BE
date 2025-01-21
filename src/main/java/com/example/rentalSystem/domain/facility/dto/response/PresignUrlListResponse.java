package com.example.rentalSystem.domain.facility.dto.response;

import java.util.List;

public record PresignUrlListResponse(List<String> presignedUrlList) {

    public static PresignUrlListResponse from(List<String> presignedUrlList) {
        return new PresignUrlListResponse(presignedUrlList);
    }
}
