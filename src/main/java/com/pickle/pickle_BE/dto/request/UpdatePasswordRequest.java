package com.pickle.pickle_BE.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {
    private String email;
    private String tempPassword;
    private String newPassword;
    private String confirmPassword;
}