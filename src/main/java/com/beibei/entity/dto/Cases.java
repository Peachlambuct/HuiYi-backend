package com.beibei.entity.dto;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@Data
@TableName("cases")
public class Cases implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JsonProperty("CreatedAt")
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @JsonProperty("UpdatedAt")
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @JsonProperty("DeletedAt")
    @TableField("deleted_at")
    private LocalDateTime deletedAt;

    @TableField("patient_id")
    private Long patientId;

    @TableField("doctor_id")
    private Long doctorId;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

    @TableField("status")
    private Boolean status;

    @TableField("check_id")
    private String checkId;
}
