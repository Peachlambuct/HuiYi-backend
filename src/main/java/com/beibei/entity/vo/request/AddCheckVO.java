package com.beibei.entity.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddCheckVO {
    @JsonProperty("case_id")
    private Long caseId;
    @JsonProperty("patient_id")
    private Long patientId;
    @JsonProperty("check_id")
    private Long checkId;
    @JsonProperty("doctor_id")
    private String doctorId;
}
