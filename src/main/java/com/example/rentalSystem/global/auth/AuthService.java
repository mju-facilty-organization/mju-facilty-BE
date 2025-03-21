package com.example.rentalSystem.global.auth;


import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import com.example.rentalSystem.global.auth.jwt.component.JwtTokenProvider;
import com.example.rentalSystem.global.auth.jwt.entity.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtToken createAuthenticationToken(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            email, password);
        try {
            Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
            return jwtTokenProvider.generateToken(authentication);
        } catch (Exception e) {
            throw new CustomException(ErrorType.FAIL_AUTHENTICATION);
        }
    }
}
