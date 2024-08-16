package com.pickle.pickle_BE.controller;

import com.pickle.pickle_BE.dto.request.ReviewRequest;
import com.pickle.pickle_BE.dto.response.ReviewResponse;
import com.pickle.pickle_BE.entity.Review;
import com.pickle.pickle_BE.entity.User;
import com.pickle.pickle_BE.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ReviewRequest reviewRequest) {
        if (userDetails == null) { return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);}
        String userId = ((User) userDetails).getUserId(); // UserDetails에서 사용자 ID를 가져옵니다.
        Review review = Review.builder()
                .reviewsPlaceId(reviewRequest.getReviewsPlaceId())
                .userId(userId) // 토큰에서 가져온 userId 설정
                .rating(reviewRequest.getRating())
                .reviewText(reviewRequest.getReviewText()) // 리뷰 텍스트 추가
                .build();

        Review savedReview = reviewService.saveReview(review);

        ReviewResponse response = ReviewResponse.builder()
                .reviewId(savedReview.getReviewId())
                .userId(savedReview.getUserId())
                .reviewsPlaceId(savedReview.getReviewsPlaceId())
                .rating(savedReview.getRating())
                .reviewText(savedReview.getReviewText())
                .reviewDate(savedReview.getReviewDate())
                .build();

        return ResponseEntity.status(201).body(response);
    }

    // 리뷰 조회
    @GetMapping("/list")
    public ResponseEntity<List<ReviewResponse>> getReviewByUserId(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) { return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); }
        String userId = ((User) userDetails).getUserId(); // UserDetails에서 사용자 ID를 가져옵니다.
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        if (!reviews.isEmpty()) {
            List<ReviewResponse> responseList = reviews.stream()
                    .map(review -> ReviewResponse.builder()
                            .reviewId(review.getReviewId())
                            .userId(review.getUserId())
                            .reviewsPlaceId(review.getReviewsPlaceId())
                            .rating(review.getRating())
                            .reviewText(review.getReviewText())
                            .reviewDate(review.getReviewDate())
                            .build())
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responseList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 개별 리뷰 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable Long reviewId) {
        Optional<Review> review = reviewService.getReviewById(reviewId);
        if (review.isPresent()) {
            ReviewResponse response = ReviewResponse.builder()
                    .reviewId(review.get().getReviewId())
                    .userId(review.get().getUserId())
                    .reviewsPlaceId(review.get().getReviewsPlaceId())
                    .rating(review.get().getRating())
                    .reviewText(review.get().getReviewText())
                    .reviewDate(review.get().getReviewDate())
                    .build();
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
