package com.pickle.pickle_BE.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "reviews_place_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewsPlaceData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviews_place_id")
    private Long reviewsPlaceId;

    @Column(name = "theme", length = 10, nullable = false)
    private String theme;

    @Column(name = "place_name", length = 50, nullable = false)
    private String placeName;

    @Column(name = "review_data", length = 255, nullable = false)
    private String reviewData;

    @Column(name = "review_count", length = 255, nullable = false)
    private String reviewCount;

    @Builder
    public ReviewsPlaceData(String theme, String placeName, String reviewData, String reviewCount) {
        this.theme = theme;
        this.placeName = placeName;
        this.reviewData = reviewData;
        this.reviewCount = reviewCount;
    }
}