package com.beibei.entity.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 病例详情VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseInfoVO {
    /**
     * 病例ID
     */
    @JsonProperty("ID")
    private Long id;

    @JsonProperty("CreatedAt")
    private LocalDateTime createdAt;

    @JsonProperty("UpdatedAt")
    private LocalDateTime updatedAt;

    @JsonProperty("DeletedAt")
    private LocalDateTime deletedAt;

    private Long patientId;

    private Long doctorId;

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 医生科室
     */
    private String doctorType;

    /**
     * 医生ID
     */
    private String doctorIdStr;

    /**
     * 检查项目列表
     */
    private List<CheckItemVO> checkProject;

    /**
     * 病例内容
     */
    private String content;

    /**
     * 患者性别
     */
    private Boolean sex;

    /**
     * 病例标题
     */
    private String title;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 患者ID
     */
    private String patientIdStr;

    /**
     * 患者年龄
     */
    private Integer age;

    /**
     * 日期
     */
    private String date;

    /**
     * 检查ID
     */
    private String checkId;

    private PatientInfoVO patientInfoVO;
}
