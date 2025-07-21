package com.example.rentalSystem.global.auth.security;

import com.example.rentalSystem.domain.member.base.entity.Member;
import com.example.rentalSystem.domain.member.base.implement.MemberLoader;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberLoader.findByEmail(email);
        return new CustomerDetails(member);
    }
}
