package com.example.rentalSystem.domain.notice.dto.request;

import com.example.rentalSystem.domain.notice.domain.entity.Notice;

public record CreateNoticeRequest(
    String title,
    String content,
    String imageName
) {

    public Notice toEntity(String imageUrl) {
        return Notice.builder()
            .title(title)
            .content(content)
            .imageUrl(imageUrl)
            .build();
    }

}
