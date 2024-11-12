package com.beibei.entity.vo.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientInfoVO {
    private Long id;
    private Long userId;
    private String name;
    private float height;
    private float weight;
    private int age;
    private String sex;
    @JsonProperty("medical_history")
    private String medicalHistory;
    private String phone;
    private String address;
    private String allergens;
    private String role;
    private String avatar;
    private String username;
    private LocalDateTime birthday;
}
