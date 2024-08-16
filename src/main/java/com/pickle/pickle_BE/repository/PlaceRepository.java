package com.pickle.pickle_BE.repository;

import com.pickle.pickle_BE.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByUserId(String userId);

    Optional<Place> findByUserIdAndPlaceNameAndLatAndLng(String userId, String placeName, double lat, double lng);
}
