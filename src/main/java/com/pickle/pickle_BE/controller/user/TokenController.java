package com.pickle.pickle_BE.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickle.pickle_BE.config.jwt.TokenProvider;
import com.pickle.pickle_BE.dto.response.LoginUserResponse;
import com.pickle.pickle_BE.entity.RefreshToken;
import com.pickle.pickle_BE.entity.User;
import com.pickle.pickle_BE.service.RefreshTokenService;
import com.pickle.pickle_BE.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenController {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private static final Logger logger = Logger.getLogger(TokenProvider.class.getName());

    @PostMapping("/refresh")
    public void refreshAccessToken(@RequestBody Map<String, String> request, HttpServletResponse response) throws IOException {
        String refreshToken = request.get("refreshToken");

        if (!tokenProvider.validToken(refreshToken)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(new LoginUserResponse(null, "Invalid refresh token")));
            return;
        }

        String userId = tokenProvider.getUserId(refreshToken);
        Optional<RefreshToken> storedRefreshTokenOpt = refreshTokenService.findByUserId(userId);
        if (storedRefreshTokenOpt.isEmpty() || !storedRefreshTokenOpt.get().getToken().equals(refreshToken)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(new LoginUserResponse(null, "Refresh token mismatch or not found")));
            return;
        }

        User user = userService.findById(userId);
        String newAccessToken = tokenProvider.generateToken(user, Duration.ofHours(6)); //access token 유효기간 6시간 설정

        // 응답 헤더에 Authorization 토큰 추가
        response.addHeader("Authorization", "Bearer " + newAccessToken);
        response.setContentType("application/json");

        // 응답 바디에 JSON 형식으로 작성
        response.getWriter().write(new ObjectMapper().writeValueAsString(new LoginUserResponse(newAccessToken, refreshToken)));
    }
}