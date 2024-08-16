package com.pickle.pickle_BE.controller;

import com.pickle.pickle_BE.dto.request.PreferenceRequest;
import com.pickle.pickle_BE.entity.Preference;
import com.pickle.pickle_BE.entity.User;
import com.pickle.pickle_BE.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/preferences")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService preferenceService;
    // Preference 등록
    @PostMapping("/batch")
    public ResponseEntity<String> savePreferences(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PreferenceRequest preferenceRequest) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = ((User) userDetails).getUserId();

        // 이미 존재하는 Preference가 있는지 확인
        Preference existingPreference = preferenceService.getPreferencesByUserId(userId);
        if (existingPreference != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User already has preferences. Consider updating instead.");
        }

        try {
            preferenceService.saveAllPreferences(userId, preferenceRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process preference data");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Preferences saved successfully");
    }

    // Preference 조회
    @GetMapping
    public ResponseEntity<Preference> getPreferences(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = ((User) userDetails).getUserId();
        Preference preferences = preferenceService.getPreferencesByUserId(userId);

        if (preferences == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(preferences);
    }

    // Preference 수정
    @PutMapping
    public ResponseEntity<String> updatePreferences(
            @RequestBody PreferenceRequest preferenceRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = ((User) userDetails).getUserId();

        try {
            preferenceService.updatePreferences(preferenceRequest, userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process preference data");
        }

        return ResponseEntity.ok("Preferences updated successfully");
    }
}