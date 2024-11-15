package com.beibei.entity.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountResponseVO {
    @JsonProperty("doctor_count")
    private Long doctorCount;
    @JsonProperty("patient_count")
    private Long patientCount;
    @JsonProperty("check_project_count")
    private Long checkProjectCount;
}
