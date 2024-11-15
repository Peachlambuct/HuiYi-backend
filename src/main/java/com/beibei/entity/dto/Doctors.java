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
@TableName("doctors")
public class Doctors implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField("deleted_at")
    private LocalDateTime deletedAt;

    @TableField("user_id")
    private Long userId;

    @TableField("name")
    private String name;

    @TableField("honor")
    private String honor;

    @TableField("job_title")
    @JsonProperty("job_title")
    private String jobTitle;

    @TableField("job_type")
    @JsonProperty("job_type")
    private String jobType;

    @TableField("phone")
    private String phone;
}
