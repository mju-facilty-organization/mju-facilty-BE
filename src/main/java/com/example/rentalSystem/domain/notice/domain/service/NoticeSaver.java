package com.example.rentalSystem.domain.notice.domain.service;

import com.example.rentalSystem.domain.notice.domain.entity.Notice;
import com.example.rentalSystem.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoticeSaver {

    private final NoticeRepository noticeRepository;

    public Long save(Notice notice) {
        Notice save = noticeRepository.save(notice);
        return save.getId();
    }
}
