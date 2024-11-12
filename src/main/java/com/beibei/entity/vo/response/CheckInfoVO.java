package com.beibei.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckInfoVO {
    private Long id;
    private String name;
    private String room;
    private String status;
    private LocalDateTime createdAt;
}
