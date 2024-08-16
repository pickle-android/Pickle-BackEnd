package com.pickle.pickle_BE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterUserResponse {
    private String userId;
    private String email;
    private String name;
}
