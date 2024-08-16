package com.pickle.pickle_BE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class LoginUserResponse {
    private String accessToken;
    private String refreshToken;
}
