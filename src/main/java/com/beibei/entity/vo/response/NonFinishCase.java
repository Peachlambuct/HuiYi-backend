package com.beibei.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NonFinishCase {
    private Integer id;
    private String patientName;
    private Integer age;
    private LocalDateTime birthday;
    private LocalDateTime updatedAt;
    private String sex;
}
