package com.example.rentalSystem.global.auth.jwt;

import com.example.rentalSystem.global.auth.jwt.component.JwtTokenProvider;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(
        ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        try {
            String token = resolveToken(request);
            jwtTokenProvider.checkValidToken(token);
            filterChain.doFilter(servletRequest, servletResponse);
        }
        catch (CustomException e) {
            handleException((HttpServletResponse) servletResponse, e);
            // 예외 처리 로직
        }

    }
    private void handleException(HttpServletResponse response, CustomException e) throws IOException {

        if (e != null) {
            response.setContentType("application/json");
            response.setStatus(400);
            response.setCharacterEncoding("UTF-8");
            String jsonResponse = new ObjectMapper().writeValueAsString(ApiResponse.error(e.getErrorType()));
            response.getWriter().write(jsonResponse);  // ApiResponse의 내용을 JSON으로 변환하여 작성
        } else {
            response.getWriter().write("{\"error\": \"An unexpected error occurred.\"}");
        }
    }
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}