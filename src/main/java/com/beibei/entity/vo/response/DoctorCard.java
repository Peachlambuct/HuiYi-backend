package com.beibei.entity.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DoctorCard {
    private Long id;
    private String name;
    private String honor;
    @JsonProperty("job_title")
    private String jobTitle;
    @JsonProperty("job_type")
    private String jobType;
    private String phone;
    private String img;
}
