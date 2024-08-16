package com.pickle.pickle_BE.config.oauth;

import com.pickle.pickle_BE.config.jwt.TokenProvider;
import com.pickle.pickle_BE.entity.RefreshToken;
import com.pickle.pickle_BE.entity.User;
import com.pickle.pickle_BE.repository.UserRepository;
import com.pickle.pickle_BE.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        log.info("OAuth2 authentication successful for email: {}", email);

        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("User not found for email: {}", email);
            return new RuntimeException("User not found");
        });

        log.info("User found: {}", user.getUsername());

        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(1));
        log.info("Generated access token: {}", accessToken);

        String refreshToken;

        Optional<RefreshToken> existingRefreshToken = refreshTokenService.findByUserId(user.getUserId());

        if (existingRefreshToken.isPresent()) {
            refreshToken = existingRefreshToken.get().getToken();
            log.info("Reusing existing refresh token: {}", refreshToken);
        } else {
            refreshToken = tokenProvider.generateToken(user, Duration.ofDays(30)); // refresh token 유효 기간 설정
            RefreshToken refreshTokenEntity = new RefreshToken(user.getUserId(), refreshToken);
            refreshTokenService.save(refreshTokenEntity);
            log.info("Generated new refresh token: {}", refreshToken);
        }

        // 안드로이드 앱으로 리디렉션할 URI 설정
        String redirectUri = "myapp://callback";
        String redirectUrl = String.format("%s?access_token=%s&refresh_token=%s", redirectUri, accessToken, refreshToken);

        log.info("Redirecting to: {}", redirectUrl);

        // 클라이언트를 안드로이드 앱으로 리디렉션
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Authentication context set successfully for user: {}", user.getUsername());
    }
}