package com.pickle.pickle_BE.controller.user;

import com.pickle.pickle_BE.dto.request.UpdatePasswordRequest;
import com.pickle.pickle_BE.entity.User;
import com.pickle.pickle_BE.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //email 중복 확인
    @PostMapping("/check-email")
    public ResponseEntity<String> checkEmailDuplicate(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (userService.getEmailByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use.");  // 409 CONFLICT 반환
        } else {
            return ResponseEntity.ok("Email is available.");  // 200 OK 반환
        }
    }

    //아이디 찾기 phoneNumber해당하는 id있으면 반환, 없으면 404
    @GetMapping("/id/{phoneNumber}")
    public ResponseEntity<String> getEmailByPhoneNumber(@PathVariable String phoneNumber) {
        Optional<String> email = userService.getEmailByPhoneNumber(phoneNumber);
        if (email.isPresent()) {
            return ResponseEntity.ok(email.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    //비밀번호 변경
    @PatchMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequest request) {
        // 이메일로 기존 비밀번호를 조회
        String existingPassword = userService.getPasswordByEmail(request.getEmail())
                .orElse(null);
        // 기존 비밀번호가 없거나 임시 비밀번호가 일치하지 않으면 에러 반환
        if (existingPassword == null || !userService.validateTemporaryPassword(request.getEmail(), request.getTempPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid temporary password");  // 400 BAD REQUEST 반환
        }
        // 새로운 비밀번호와 확인 비밀번호가 일치하지 않으면 에러 반환
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New passwords do not match");  // 400 BAD REQUEST 반환
        }
        // 비밀번호 해시화
        String newPasswordHash = bCryptPasswordEncoder.encode(request.getNewPassword());
        // 비밀번호 업데이트
        userService.updatePassword(request.getEmail(), newPasswordHash);
        return ResponseEntity.ok("Password updated successfully");  // 200 OK 반환
    }

    //user 정보 조회
    @GetMapping("/me")
    public ResponseEntity<Object> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");  // 401 UNAUTHORIZED 반환
        }

        String userId = ((User) userDetails).getUserId();  // 토큰에서 UserId 추출
        Optional<User> userOptional = userService.getUserByUserId(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getUserId());
            response.put("email", user.getEmail());
            response.put("name", user.getName());
            response.put("phoneNumber", user.getPhoneNumber());
            return ResponseEntity.ok(response);  // 200 OK 반환
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");  // 404 NOT FOUND 반환
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> UpdateUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody User userUpdateRequest) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");  // 401 UNAUTHORIZED 반환
        }
        String userId = ((User) userDetails).getUserId();  // 토큰에서 UserId 추출
        if (userService.getUserByUserId(userId).isPresent()) {
            userService.updateUserById(userId, userUpdateRequest);
            return ResponseEntity.ok("User updated successfully");  // 200 OK 반환
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not found");  // 403 FORBIDDEN 반환
        }
    }
}