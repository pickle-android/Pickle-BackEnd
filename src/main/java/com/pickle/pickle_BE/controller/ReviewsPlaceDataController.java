package com.pickle.pickle_BE.controller;

import com.pickle.pickle_BE.entity.ReviewsPlaceData;
import com.pickle.pickle_BE.repository.ReviewsPlaceDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewsPlaceDataController {
    private final ReviewsPlaceDataRepository reviewsPlaceDataRepository;

    @GetMapping("/all")
    public ResponseEntity<List<ReviewsPlaceData>> getAllReviewsPlaceData() {
        List<ReviewsPlaceData> reviewsPlaceDataList = reviewsPlaceDataRepository.findAll();
        return new ResponseEntity<>(reviewsPlaceDataList, HttpStatus.OK);
    }
}