package com.pickle.pickle_BE.controller;

import com.pickle.pickle_BE.dto.request.PlaceRequest;
import com.pickle.pickle_BE.dto.response.PlaceResponse;
import com.pickle.pickle_BE.entity.Place;
import com.pickle.pickle_BE.entity.User;
import com.pickle.pickle_BE.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceController {

    private final PlaceService placeService;

    //Place 생성 및 삭제
    @PostMapping
    public ResponseEntity<String> createOrDeletePlace(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PlaceRequest placeRequest) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }
        String userId = ((User) userDetails).getUserId(); // JWT에서 추출한 사용자 ID

        // 새로운 Place 객체 생성
        Place place = Place.builder()
                .placeName(placeRequest.getPlaceName())
                .userId(userId)
                .lat(placeRequest.getLat())
                .lng(placeRequest.getLng())
                .build();

        try {
            String message = placeService.saveOrDeletePlace(place);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }


    // Place 개별 조회 (GET)
    @GetMapping("/{id}")
    public ResponseEntity<PlaceResponse> getPlaceById(@PathVariable Long id) {
        return placeService.getPlaceById(id)
                .map(place -> {
                    PlaceResponse responseDto = PlaceResponse.builder()
                            .placeId(place.getPlaceId())
                            .placeName(place.getPlaceName())
                            .lat(place.getLat())
                            .lng(place.getLng())
                            .userId(place.getUserId())
                            .build();
                    return new ResponseEntity<>(responseDto, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Place 목록 조회 (GET)
    @GetMapping("/list")
    public ResponseEntity<List<PlaceResponse>> getAllPlaces(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = ((User) userDetails).getUserId(); // JWT에서 추출한 사용자 ID

        List<Place> places = placeService.getAllPlacesByUserId(userId);
        List<PlaceResponse> responseDtos = places.stream()
                .map(place -> PlaceResponse.builder()
                        .placeId(place.getPlaceId())
                        .placeName(place.getPlaceName())
                        .lat(place.getLat())
                        .lng(place.getLng())
                        .userId(place.getUserId())
                        .build())
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    // Place 삭제 (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
