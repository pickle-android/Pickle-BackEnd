package com.pickle.pickle_BE.service;

import com.pickle.pickle_BE.entity.Review;
import com.pickle.pickle_BE.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // 리뷰 저장 메서드
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    // 특정 유저의 리뷰 조회 메서드
    public List<Review> getReviewsByUserId(String userId) {
        return reviewRepository.findByUserId(userId);
    }

    // 개별 리뷰 조회 메서드
    public Optional<Review> getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }
}
