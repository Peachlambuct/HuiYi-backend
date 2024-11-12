package com.beibei.entity.vo.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePatientVO {
    private String name;
    private float height;
    private float weight;
    private boolean sex;
    @JsonAlias("medical_history")
    private String medicalHistory;
    private String phone;
    private String address;
    private String allergens;
    private LocalDateTime birthday;
}
