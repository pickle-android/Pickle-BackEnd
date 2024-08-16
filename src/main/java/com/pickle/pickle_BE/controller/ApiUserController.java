package com.pickle.pickle_BE.controller;

import com.pickle.pickle_BE.entity.Preference;
import com.pickle.pickle_BE.entity.Review;
import com.pickle.pickle_BE.entity.User;
import com.pickle.pickle_BE.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class ApiUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/preferences")
    public ResponseEntity<List<Preference>> getUserPreferences(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String userId = ((User) userDetails).getUserId(); // UserDetails에서 사용자 ID를 가져옴
        List<Preference> preferences = userService.getPreferencesByUserId(userId);
        return ResponseEntity.ok(preferences);
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<Review>> getUserReviews(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String userId = ((User) userDetails).getUserId(); // UserDetails에서 사용자 ID를 가져옴
        List<Review> reviews = userService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }
}
