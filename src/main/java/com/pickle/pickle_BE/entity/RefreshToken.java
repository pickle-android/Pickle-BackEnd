package com.pickle.pickle_BE.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "token", nullable = false, length = 512)
    private String token;

    public RefreshToken(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public RefreshToken update(String newToken) {
        this.token = newToken;
        return this;
    }
}