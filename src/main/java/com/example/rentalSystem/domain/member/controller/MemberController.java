package com.example.rentalSystem.domain.member.controller;

import com.example.rentalSystem.domain.member.controller.dto.response.MemberInfoResponse;
import com.example.rentalSystem.domain.member.entity.CustomerDetails;
import com.example.rentalSystem.domain.member.service.MemberService;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.type.SuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController implements MemberControllerDocs {

    private final MemberService memberService;

    @Override
    @GetMapping
    public ApiResponse<MemberInfoResponse> getMemberInfo(
        @AuthenticationPrincipal CustomerDetails customerDetails
    ) {
        String email = customerDetails.getUsername();
        return ApiResponse.success(SuccessType.SUCCESS, memberService.getMemberInfoByEmail(email));
    }
}
