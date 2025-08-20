package com.example.rentalSystem.domain.notice.domain.service;

import com.example.rentalSystem.domain.notice.domain.entity.Notice;
import com.example.rentalSystem.domain.notice.repository.NoticeRepository;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoticeReader {

    private final NoticeRepository noticeRepository;

    public Notice getById(Long noticeId) {
        return noticeRepository.findById(noticeId).orElseThrow(
            () -> new CustomException(ErrorType.ENTITY_NOT_FOUND)
        );
    }

    public Notice getRecentlyNotice() {
        return noticeRepository.findFirstByOrderByCreatedAtDesc().orElseThrow(
            () -> new CustomException(ErrorType.ENTITY_NOT_FOUND)
        );
    }
}
