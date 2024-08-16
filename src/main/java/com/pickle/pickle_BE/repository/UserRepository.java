package com.pickle.pickle_BE.repository;

import com.pickle.pickle_BE.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findEmailByPhoneNumber(String phoneNumber);
    Optional<User> findPhoneNumberByUserId(String userId);

    Optional<User> findById(String userId);


    User findByPhoneNumberAndEmail(String phoneNumber, String email);
    @Transactional
    void deleteById(String userId);
}