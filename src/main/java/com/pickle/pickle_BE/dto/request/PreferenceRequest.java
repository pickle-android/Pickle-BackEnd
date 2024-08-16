package com.pickle.pickle_BE.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreferenceRequest {
    private List<String> data1;
    private List<String> data2;
    private List<String> data3;
    private String data4;  // 단일 값
}
