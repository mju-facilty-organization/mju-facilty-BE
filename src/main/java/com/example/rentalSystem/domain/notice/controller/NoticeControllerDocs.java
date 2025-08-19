package com.example.rentalSystem.domain.notice.controller;

import com.example.rentalSystem.domain.notice.dto.NoticeDetailsDto;
import com.example.rentalSystem.domain.notice.dto.request.CreateNoticeRequest;
import com.example.rentalSystem.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "공지사항 관련 API", description = "공지사항 관련 API")
public interface NoticeControllerDocs {

    @Operation(summary = "공지 상세 조회", description = "상세 조회")
    @GetMapping("/{noticeId}")
    ApiResponse<NoticeDetailsDto> getNoticeDetails(
        @PathVariable(name = "noticeId") Long noticeId
    );

    @GetMapping()
    @Operation(summary = "가장 최근 공지 한개 조회", description = "가장 최근 공지 조회")
    ApiResponse<NoticeDetailsDto> getRecentlyNotice();

    @PostMapping
    @Operation(summary = "공지글 등록하기", description = "제목, 내용, 이미지 파일")
    ApiResponse<?> createNotice(
        @RequestBody CreateNoticeRequest createNoticeRequest
    );
}
