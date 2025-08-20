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
        // HTML 템플릿을 사용하여 내용과 이미지를 더 예쁘게 만듭니다.
        String htmlTemplate = "<!DOCTYPE html>"
            + "<html lang=\"ko\">"
            + "<head>"
            + "<meta charset=\"UTF-8\">"
            + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
            + "<title>공지사항</title>"
            + "<style>"
            + "body { font-family: 'Malgun Gothic', 'Apple SD Gothic Neo', sans-serif; background-color: #f4f4f9; color: #333; margin: 0; padding: 20px; }"
            + ".notice-container { max-width: 800px; margin: 20px auto; padding: 30px; background-color: #fff; border-radius: 12px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08); }"
            + ".notice-content { font-size: 16px; line-height: 1.8; margin-bottom: 25px; color: #555; }"
            + ".notice-image-container { display: flex; justify-content: center; align-items: center; gap: 25px; flex-direction: column; }"
            + ".notice-image { max-width: 100%; height: auto; border-radius: 8px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); }"
            + "</style>"
            + "</head>"
            + "<body>"
            + "<div class=\"notice-container\">"
            + "<div class=\"notice-content\">"
            + "    <p>" + content + "</p>"
            + "</div>";

        if (presignedUrlForGet != null && !presignedUrlForGet.isEmpty()) {
            htmlTemplate += "<div class=\"notice-image-container\">"
                + "    <img src='" + presignedUrlForGet + "' alt='공지사항 이미지' class='notice-image'/>"
                + "</div>";
        }

        htmlTemplate += "</div>"
            + "</body>"
            + "</html>";

        combinedHtml = htmlTemplate;

        return NoticeDetailsDto.builder()
            .id(notice.getId())
            .title(notice.getTitle())
            .htmlContent(combinedHtml)
            .createAt(notice.getCreatedAt())
            .build();
    }

}
