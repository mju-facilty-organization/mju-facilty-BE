package com.example.rentalSystem.global.auth.security;

import com.example.rentalSystem.domain.member.entity.CustomerDetails;
import com.example.rentalSystem.domain.member.entity.Member;
import com.example.rentalSystem.domain.member.repository.MemberRepository;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(()-> new CustomException(
            ErrorType.ENTITY_NOT_FOUND));
        return new CustomerDetails(member);
    }
}
