package com.pickle.pickle_BE.service;

import com.pickle.pickle_BE.entity.RefreshToken;
import com.pickle.pickle_BE.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }
    public boolean deleteByUserId(String userId) {
        try {
            refreshTokenRepository.deleteByUserId(userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<RefreshToken> findByUserId(String userId) {
        return refreshTokenRepository.findByUserId(userId);
    }
}