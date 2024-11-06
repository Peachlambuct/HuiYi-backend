package com.beibei.entity.dto;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("appointments")
public class Appointments implements Serializable {

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

    @TableField("time_id")
    private Long timeId;

    @TableField("patient_id")
    private Long patientId;

    @TableField("doctor_id")
    private Long doctorId;

    @TableField("year")
    private String year;

    @TableField("month")
    private String month;

    @TableField("day")
    private String day;

    @TableField("status")
    private Boolean status;
}
