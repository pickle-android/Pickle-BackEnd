package com.pickle.pickle_BE.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickle.pickle_BE.dto.request.PreferenceRequest;
import com.pickle.pickle_BE.entity.Preference;
import com.pickle.pickle_BE.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Preference 등록
    public void saveAllPreferences(String userId, PreferenceRequest preferenceRequest) {
        try {
            String data1 = objectMapper.writeValueAsString(preferenceRequest.getData1());
            String data2 = objectMapper.writeValueAsString(preferenceRequest.getData2());
            String data3 = objectMapper.writeValueAsString(preferenceRequest.getData3());
            String data4 = preferenceRequest.getData4();

            Preference preference = Preference.builder()
                    .userId(userId)
                    .data1(data1)
                    .data2(data2)
                    .data3(data3)
                    .data4(data4)
                    .build();

            preferenceRepository.save(preference);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert list to JSON string", e);
        }
    }

    // Preference 조회 (그대로 문자열로 반환)
    public Preference getPreferencesByUserId(String userId) {
        return preferenceRepository.findByUserId(userId);
    }

    // Preference 수정
    public void updatePreferences(PreferenceRequest preferenceRequest, String userId) {
        Preference preference = preferenceRepository.findByUserId(userId);
        if (preference == null) {
            throw new IllegalArgumentException("No preferences found for this user");
        }

        try {
            String data1 = objectMapper.writeValueAsString(preferenceRequest.getData1());
            String data2 = objectMapper.writeValueAsString(preferenceRequest.getData2());
            String data3 = objectMapper.writeValueAsString(preferenceRequest.getData3());
            String data4 = preferenceRequest.getData4();

            preference.setData1(data1);
            preference.setData2(data2);
            preference.setData3(data3);
            preference.setData4(data4);

            preferenceRepository.save(preference);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert list to JSON string", e);
        }
    }
}