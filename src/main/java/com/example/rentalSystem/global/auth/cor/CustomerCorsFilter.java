package com.example.rentalSystem.global.auth.cor;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomerCorsFilter implements Filter {

    @Override
    public void doFilter(
        ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        // CORS 허용한 Origin
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // 자격이 포함된 요청 받기
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH,DELETE, OPTIONS");
        // http 메서드 전체 다 허용
        response.setHeader("Access-Control-Max-Age", "3600");
        // 프리플라이트(preflight) 요청의 캐시 시간을 정의합니다.
        response.setHeader("Access-Control-Allow-Headers",
            "Origin, X-Requested-With, Content-Type, Accept, Authorization");
        // 이 헤더는 클라이언트가 요청 시 사용할 수 있는 HTTP 헤더를 정의합니다.

        if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        }else {
            chain.doFilter(req, res);
        }
    }
}
