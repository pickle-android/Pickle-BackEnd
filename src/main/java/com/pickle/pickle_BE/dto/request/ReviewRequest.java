package com.pickle.pickle_BE.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    private Long reviewsPlaceId; // 리뷰할 장소 ID
    private byte rating;         // 평점 (1~10 사이)
    private String reviewText;   // 리뷰 텍스트
}
