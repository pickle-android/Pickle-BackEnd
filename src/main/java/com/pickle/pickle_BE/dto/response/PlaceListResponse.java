package com.pickle.pickle_BE.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PlaceListResponse {
    private List<PlaceResponse> places;
}
