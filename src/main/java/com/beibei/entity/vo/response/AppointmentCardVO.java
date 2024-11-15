package com.beibei.entity.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCardVO {
    private int id;
    @JsonProperty("doctor_name")
    private String doctorName;
    @JsonProperty("doctor_title")
    private String doctorTitle;
    @JsonProperty("doctor_type")
    private String doctorType;
    @JsonProperty("doctor_avatar")
    private String doctorAvatar;
    private String date;
    private boolean status;
    private int year;
    private int month;
    private int day;
    private int timeId;
}
