package com.beibei.entity.vo.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCaseInfoVO {
    private Long id;
    private String title;
    private String content;
    @JsonProperty("doctor_name")
    private String doctorName;
    @JsonProperty("check_project")
    private List<CheckInfo> checkProject;
    private String checkId;
    @JsonProperty("patient_name")
    private String patientName;
    @JsonProperty("patient_id")
    private Long patientId;
    private boolean sex;
    private int age;
    private String doctorType;
    private String date;
    private Long doctorId;
}
