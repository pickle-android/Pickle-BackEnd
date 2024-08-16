package com.pickle.pickle_BE.config.jwt;

import com.pickle.pickle_BE.entity.User;
import com.pickle.pickle_BE.service.UserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private static final Logger logger = Logger.getLogger(TokenProvider.class.getName());

    private final UserDetailService userDetailService;
    private final JwtProperties jwtProperties;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
    }

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    private String makeToken(Date expiry, User user) {
        Date now = new Date();
        String token = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getEmail())
                .claim("id", user.getUserId())
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        logger.info("Generated Token: " + token);
        return token;
    }

    public boolean validToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            logger.info("Token is valid: " + token);
            return true;
        } catch (Exception e) {
            logger.warning("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }

    // 토큰으로부터 인증 정보 추출 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String userId = claims.get("id", String.class);
        UserDetails userDetails = userDetailService.loadUserByUserId(userId); // 변경된 부분
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public String getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", String.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}