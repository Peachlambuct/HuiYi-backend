package com.beibei.entity.vo.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class CreateAppointmentVO {
    @JsonAlias("doctor_id")
    private Long doctorId;
    @JsonAlias("time_id")
    private Integer timeId;
    private String time;
}
