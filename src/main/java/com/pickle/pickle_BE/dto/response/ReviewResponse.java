package com.pickle.pickle_BE.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponse {

    private Long reviewId;        // 리뷰 ID
    private String userId;        // 리뷰 작성자 ID
    private Long reviewsPlaceId;  // 리뷰한 장소 ID
    private byte rating;          // 평점
    private String reviewText;    // 리뷰 텍스트
    private LocalDateTime reviewDate; // 리뷰 작성일
}
