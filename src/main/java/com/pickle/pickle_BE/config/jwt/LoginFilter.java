package com.pickle.pickle_BE.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickle.pickle_BE.dto.request.LoginUserRequest;
import com.pickle.pickle_BE.dto.response.LoginUserResponse;
import com.pickle.pickle_BE.entity.RefreshToken;
import com.pickle.pickle_BE.entity.User;
import com.pickle.pickle_BE.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginUserRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginUserRequest.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword(), Collections.emptyList());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        User user = (User) authResult.getPrincipal();
        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(6)); // access token 유효기간 6시간 설정

        // 사용자 ID로 Refresh Token 조회
        Optional<RefreshToken> existingRefreshToken = refreshTokenService.findByUserId(user.getUserId());
        String refreshToken;

        if (existingRefreshToken.isPresent()) {
            // Refresh Token이 존재하면 해당 토큰을 사용
            refreshToken = existingRefreshToken.get().getToken();
        } else {
            // Refresh Token이 존재하지 않으면 새로운 토큰을 생성
            refreshToken = tokenProvider.generateToken(user, Duration.ofDays(60)); // refresh token 유효기간 60일 설정
            RefreshToken refreshTokenEntity = new RefreshToken(user.getUserId(), refreshToken);
            refreshTokenService.save(refreshTokenEntity);
        }

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(new LoginUserResponse(accessToken, refreshToken)));
    }
}