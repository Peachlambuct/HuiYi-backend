package com.beibei.entity.vo.request;

import lombok.Data;

@Data
public class CreateAppointmentVO {
    private Long doctorId;
    private Integer timeId;
    private String time;
}
