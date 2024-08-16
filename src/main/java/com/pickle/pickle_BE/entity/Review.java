package com.pickle.pickle_BE.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Data
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "reviews_place_id", nullable = false)
    private Long reviewsPlaceId;

    @Column(name = "rating", nullable = false)
    private byte rating;

    @Column(name = "review_text", length = 255, nullable = false)
    private String reviewText;

    @Column(name = "review_date", nullable = false)
    private LocalDateTime reviewDate;

    @Builder
    public Review(String userId, Long reviewsPlaceId, byte rating, String reviewText) {
        this.userId = userId;
        this.reviewsPlaceId = reviewsPlaceId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewDate = LocalDateTime.now();
    }
}
