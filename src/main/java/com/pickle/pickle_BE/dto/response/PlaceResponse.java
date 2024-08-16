package com.pickle.pickle_BE.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlaceResponse {
    private Long placeId;
    private String userId;
    private String placeName;
    private double lat;  // 위도
    private double lng;  // 경도

    @Builder
    public PlaceResponse(Long placeId, String userId, String placeName, double lat, double lng) {
        this.placeId = placeId;
        this.userId = userId;
        this.placeName = placeName;
        this.lat = lat;
        this.lng = lng;
    }
}
