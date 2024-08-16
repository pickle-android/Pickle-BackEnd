package com.pickle.pickle_BE.repository;

import com.pickle.pickle_BE.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserId(String userId);  // 특정 유저의 리뷰를 조회하는 메서드
}
