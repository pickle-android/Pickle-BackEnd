package com.pickle.pickle_BE.service;

import com.pickle.pickle_BE.entity.Place;
import com.pickle.pickle_BE.repository.PlaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;

    // Place 저장
    public Place savePlace(Place place) {
        return placeRepository.save(place);
    }

    // Place 삭제
    public void deletePlace(Long id) {
        placeRepository.deleteById(id);
    }

    // Place ID로 검색
    public Optional<Place> getPlaceById(Long id) {
        return placeRepository.findById(id);
    }

    // User ID로 모든 Place 검색
    public List<Place> getAllPlacesByUserId(String userId) {
        return placeRepository.findByUserId(userId);
    }

    @Transactional
    public String saveOrDeletePlace(Place place) {
        try {
            Optional<Place> existingPlace = placeRepository.findByUserIdAndPlaceNameAndLatAndLng(
                    place.getUserId(), place.getPlaceName(), place.getLat(), place.getLng());

            if (existingPlace.isPresent()) {
                log.info("Existing place found: {}", existingPlace.get());
                // 동일한 데이터가 이미 존재하면 삭제
                placeRepository.delete(existingPlace.get());
                return "Place deleted successfully";
            } else {
                log.info("No existing place found, saving new place: {}", place);
                // 동일한 데이터가 존재하지 않으면 저장
                placeRepository.save(place);
                return "Place created successfully";
            }
        } catch (Exception e) {
            log.error("Error occurred while saving or deleting place: ", e);
            throw e; // 예외를 다시 던져서 Controller에서 처리할 수 있도록 함
        }
    }
}
