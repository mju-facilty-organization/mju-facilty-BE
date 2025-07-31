package com.example.rentalSystem.domain.member.base.service;

import com.example.rentalSystem.domain.member.base.dto.response.MemberInfoResponse;
import com.example.rentalSystem.domain.member.base.entity.Member;
import com.example.rentalSystem.domain.member.base.repository.MemberRepository;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberInfoResponse getMemberInfoByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(
            ErrorType.ENTITY_NOT_FOUND));
        return MemberInfoResponse.builder()
            .id(member.getId())
            .name(member.getName())
            .build();
    }
}
