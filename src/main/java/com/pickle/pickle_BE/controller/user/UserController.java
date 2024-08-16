package com.pickle.pickle_BE.controller.user;

import com.pickle.pickle_BE.config.jwt.TokenProvider;
import com.pickle.pickle_BE.dto.request.RegisterUserRequest;
import com.pickle.pickle_BE.dto.response.RegisterUserResponse;
import com.pickle.pickle_BE.entity.User;
import com.pickle.pickle_BE.service.RefreshTokenService;
import com.pickle.pickle_BE.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> registerUser(@Valid @RequestBody RegisterUserRequest request) {
        RegisterUserResponse response = userService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    //로그아웃 - access토큰을 통해, userId값 갖고와서 refresh_token 테이블 값 삭제
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization header");
        }

        String userId = ((User) userDetails).getUserId(); // UserDetails에서 사용자 ID를 가져옵니다.
        boolean isDeleted = refreshTokenService.deleteByUserId(userId);
        if (isDeleted) {
            return ResponseEntity.ok("Logged out successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization header");
        }
        String userId = ((User) userDetails).getUserId(); // UserDetails에서 사용자 ID를 가져옵니다.
        try {
            userService.deleteUser(userId);
            refreshTokenService.deleteByUserId(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User deletion failed");
        }
    }
}