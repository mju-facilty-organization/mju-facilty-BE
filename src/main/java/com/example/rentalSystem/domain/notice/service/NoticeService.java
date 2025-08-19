package com.example.rentalSystem.domain.notice.service;

import com.example.rentalSystem.domain.notice.domain.entity.Notice;
import com.example.rentalSystem.domain.notice.domain.service.NoticeReader;
import com.example.rentalSystem.domain.notice.domain.service.NoticeSaver;
import com.example.rentalSystem.domain.notice.dto.NoticeDetailsDto;
import com.example.rentalSystem.domain.notice.dto.request.CreateNoticeRequest;
import com.example.rentalSystem.domain.notice.dto.response.CreateNoticeResponse;
import com.example.rentalSystem.global.cloud.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeReader noticeReader;
    private final NoticeSaver noticeSaver;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public NoticeDetailsDto getDetailsById(Long noticeId) {
        Notice notice = noticeReader.getById(noticeId);
        if (notice.hasImage()) {
            String presignedUrlForGet = s3Service.generatePresignedUrlForGet(notice.getImageUrl());
            return NoticeDetailsDto.of(notice, presignedUrlForGet);
        }
        return NoticeDetailsDto.from(notice);
    }

    @Transactional(readOnly = true)
    public NoticeDetailsDto getRecentlyNotice() {
        Notice notice = noticeReader.getRecentlyNotice();
        return NoticeDetailsDto.from(notice);
    }

    @Transactional
    public CreateNoticeResponse createNotice(CreateNoticeRequest request) {
        String presignedUrlForPut = null;
        String imageUrlKey = null;
        if (request.imageName() != null && !request.imageName().isBlank()) {
            imageUrlKey = s3Service.generateNoticesS3Key(request.imageName());
            presignedUrlForPut = s3Service.generatePresignedUrlForPut(imageUrlKey);
        }
        Notice notice = request.toEntity(imageUrlKey);

        noticeSaver.save(notice);
        return CreateNoticeResponse.builder()
            .id(notice.getId())
            .presignedUrlForPut(presignedUrlForPut)
            .build();
    }

}
