package com.example.rentalSystem.domain.notice.dto;

import com.example.rentalSystem.domain.notice.domain.entity.Notice;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record NoticeDetailsDto(
    Long id,
    String title,
    String htmlContent,
    LocalDateTime createAt
) {

    public static NoticeDetailsDto from(Notice notice) {
        return NoticeDetailsDto.builder()
            .id(notice.getId())
            .title(notice.getTitle())
            .htmlContent(notice.getContent())
            .createAt(notice.getCreatedAt())
            .build();
    }

    public static NoticeDetailsDto of(Notice notice, String presignedUrlForGet) {
        String content = notice.getContent();
        String combinedHtml;
        if (presignedUrlForGet != null && !presignedUrlForGet.isEmpty()) {
            combinedHtml = "<div>" + content + "</div>" + "<img src='" + presignedUrlForGet + "'/>";
        } else {
            combinedHtml = "<div>" + content + "</div>";
        }
        return NoticeDetailsDto.builder()
            .id(notice.getId())
            .title(notice.getTitle())
            .htmlContent(combinedHtml)
            .createAt(notice.getCreatedAt())
            .build();
    }
}
