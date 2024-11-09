package com.beibei.entity.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckVO {
    private Long doctorId;
    private Long patientId;
    private Long checkProjectId;;
    private String Status;
}
