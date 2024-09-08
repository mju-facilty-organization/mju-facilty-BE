package com.example.rentalSystem.global.auth.security;

import com.example.rentalSystem.domain.member.entity.CustomerDetails;
import com.example.rentalSystem.domain.member.entity.Member;
import com.example.rentalSystem.domain.member.implement.MemberLoader;
import com.example.rentalSystem.domain.member.repository.MemberRepository;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ErrorType;
import java.nio.file.attribute.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerDetailsService implements UserDetailsService {

    private final MemberLoader memberLoader;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Member member = memberLoader.findByLoginId(loginId);
        return new CustomerDetails(member);
    }
}
