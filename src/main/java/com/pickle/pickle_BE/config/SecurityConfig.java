package com.pickle.pickle_BE.config;

import com.pickle.pickle_BE.config.jwt.LoginFilter;
import com.pickle.pickle_BE.config.jwt.TokenAuthenticationFilter;
import com.pickle.pickle_BE.config.jwt.TokenProvider;
import com.pickle.pickle_BE.config.oauth.OAuth2UserCustomService;
import com.pickle.pickle_BE.config.oauth.OAuthSuccessHandler;
import com.pickle.pickle_BE.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshTokenService refreshTokenService;
    private final OAuth2UserCustomService oauth2UserCustomService;
    private final OAuthSuccessHandler oAuthSuccessHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/user/register", "/login", "/api/**", "/users/updatePassword","/users/id/**", "/oauth/login", "/oauth2/**", "/reviews/**").permitAll()  // 인증 없이 접근 가능
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // 관리자 로그인 페이지 ( 로그인 폼 )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .defaultSuccessUrl("/admin/dashboard")
                        .failureUrl("/admin/login?error=true")
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth/login")  // OAuth 로그인 페이지 설정
                        .userInfoEndpoint(userInfo -> userInfo.userService(oauth2UserCustomService))
                        .successHandler(oAuthSuccessHandler)
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint)  // Custom 인증 실패 설정
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 세션 관리 (Admin에만 세션 사용)
                        .maximumSessions(1)
                        .expiredUrl("/admin/login?expired")
                )
                // API 로그인 필터 설정
                .addFilterBefore(new LoginFilter(authenticationManager(authenticationConfiguration), tokenProvider, refreshTokenService),
                        UsernamePasswordAuthenticationFilter.class)
                //토큰 인증 필터 설정
                .addFilterBefore(new TokenAuthenticationFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
