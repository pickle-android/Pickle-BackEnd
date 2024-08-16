package com.pickle.pickle_BE.repository;


import com.pickle.pickle_BE.entity.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    Preference findByUserId(String userId);
    List<Preference> findListByUserId(String userId);


}
