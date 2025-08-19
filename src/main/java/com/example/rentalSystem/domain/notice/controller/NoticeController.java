package com.example.rentalSystem.domain.notice.controller;

import com.example.rentalSystem.domain.notice.dto.NoticeDetailsDto;
import com.example.rentalSystem.domain.notice.dto.request.CreateNoticeRequest;
import com.example.rentalSystem.domain.notice.dto.response.CreateNoticeResponse;
import com.example.rentalSystem.domain.notice.service.NoticeService;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.type.SuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController implements NoticeControllerDocs {

    private final NoticeService noticeService;

    @Override
    @GetMapping("/{noticeId}")
    public ApiResponse<NoticeDetailsDto> getNoticeDetails(
        @PathVariable(name = "noticeId") Long noticeId
    ) {
        NoticeDetailsDto noticeDetailsDto = noticeService.getDetailsById(noticeId);
        return ApiResponse.success(SuccessType.SUCCESS, noticeDetailsDto);
    }

    @Override
    @GetMapping()
    public ApiResponse<NoticeDetailsDto> getRecentlyNotice() {
        NoticeDetailsDto noticeDetailsDto = noticeService.getRecentlyNotice();
        return ApiResponse.success(SuccessType.SUCCESS, noticeDetailsDto);
    }

    @Override
    @PostMapping
    public ApiResponse<?> createNotice(
        @RequestBody CreateNoticeRequest createNoticeRequest
    ) {
        CreateNoticeResponse createNoticeResponse = noticeService.createNotice(createNoticeRequest);
        return ApiResponse.success(SuccessType.CREATED, createNoticeResponse);

    }
}
