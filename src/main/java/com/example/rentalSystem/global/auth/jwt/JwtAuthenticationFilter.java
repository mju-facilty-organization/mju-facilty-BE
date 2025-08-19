package com.example.rentalSystem.global.auth.jwt;

import com.example.rentalSystem.global.auth.jwt.component.JwtTokenProvider;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest servletRequest,
        @NonNull HttpServletResponse servletResponse,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = resolveToken(servletRequest);
            if (jwtTokenProvider.checkValidToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (CustomException e) {
            handleException(servletResponse, e);
            // 예외 처리 로직
        }
    }

    private void handleException(HttpServletResponse response, CustomException e)
        throws IOException {
        if (e != null) {
            response.setContentType("application/json");
            response.setStatus(400);
            response.setCharacterEncoding("UTF-8");
            String jsonResponse = new ObjectMapper().writeValueAsString(
                ApiResponse.error(e.getErrorType()));
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