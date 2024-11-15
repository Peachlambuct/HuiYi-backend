package com.beibei.entity.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LastAppointmentVO {
    @JsonProperty("doctor_name")
    private String doctorName;
    @JsonProperty("doctor_type")
    private String doctorType;
    private String date;
    @JsonProperty("doctor_img")
    private String doctorImg;
    private String updateTime;
    private int year;
    private int month;
    private int day;
    private int timeId;
}
