package com.pickle.pickle_BE.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String uri = request.getRequestURI();

        // 토큰 없이 접근 가능한 경로에 대해서는 Unauthorized 응답을 반환하지 않음
        if (uri.startsWith("/user/register") ||
                uri.startsWith("/login") ||
                uri.startsWith("/api/") ||
                uri.startsWith("/users/updatePassword") ||
                uri.startsWith("/admin/")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // 그 외 경로에 대해서는 Unauthorized 응답을 반환
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"Unauthorized access\"}");
    }
}