package com.example.rentalSystem.domain.member.base.implement;

import com.example.rentalSystem.domain.member.base.entity.Member;
import com.example.rentalSystem.domain.member.base.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberLoader {

    private final MemberRepository memberRepository;

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(
            (() -> new EntityNotFoundException(
                "member not found :" + email)));
    }
}
