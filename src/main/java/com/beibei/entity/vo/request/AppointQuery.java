package com.beibei.entity.vo.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointQuery {
    private LocalDateTime from;
    private LocalDateTime to;
    private Integer status;
}
