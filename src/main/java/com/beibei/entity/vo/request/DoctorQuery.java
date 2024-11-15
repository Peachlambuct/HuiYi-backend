package com.beibei.entity.vo.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorQuery {
    @JsonAlias("doctor_name")
    private String doctorName;
    @JsonAlias("doctor_type")
    private String doctorType;
}
