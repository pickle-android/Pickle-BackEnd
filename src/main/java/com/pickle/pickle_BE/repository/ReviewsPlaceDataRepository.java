package com.pickle.pickle_BE.repository;

import com.pickle.pickle_BE.entity.ReviewsPlaceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewsPlaceDataRepository extends JpaRepository<ReviewsPlaceData, Long> {
    List<ReviewsPlaceData> findAll();
}