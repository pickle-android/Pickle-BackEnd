package com.pickle.pickle_BE.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "preference")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preference_id", nullable = false)
    private Long preferenceId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "data1", length = 255)
    private String data1;

    @Column(name = "data2", length = 255)
    private String data2;

    @Column(name = "data3", length = 255)
    private String data3;

    @Column(name = "data4", length = 255)
    private String data4;  // 단일 값
}
