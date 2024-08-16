package com.pickle.pickle_BE.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlaceRequest {
    private String placeName;
    private double lat;  // 위도
    private double lng;  // 경도

    public PlaceRequest(String placeName, double lat, double lng) {
        this.placeName = placeName;
        this.lat = lat;
        this.lng = lng;
    }
}
