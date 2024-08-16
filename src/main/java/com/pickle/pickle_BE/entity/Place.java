package com.pickle.pickle_BE.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "place")  // 테이블 이름을 'place'로 설정
@Data
@NoArgsConstructor
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long placeId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "place_name", length = 50, nullable = false)
    private String placeName;

    @Column(name = "lat", nullable = false)
    private double lat;  // 위도

    @Column(name = "lng", nullable = false)
    private double lng;  // 경도

    @Builder
    public Place(String userId, String placeName, double lat, double lng) {
        this.userId = userId;
        this.placeName = placeName;
        this.lat = lat;
        this.lng = lng;
    }
}