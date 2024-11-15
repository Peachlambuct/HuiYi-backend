package com.beibei.entity.vo.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChoosePropVO {
    @NotNull(message = "doctorId不能为空")
    @JsonAlias("doctor_id")
    private String doctorId;
    @NotNull(message = "date不能为空")
    private String date;
}
