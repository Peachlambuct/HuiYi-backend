package com.beibei.entity.dto;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@Data
@TableName("patients")
public class Patients implements Serializable {

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

    @TableField("height")
    private Double height;

    @TableField("weight")
    private Double weight;

    @TableField("sex")
    private Boolean sex;

    /**
     * '过往病史'
     */
    @TableField("medical_history")
    private String medicalHistory;

    @TableField("phone")
    private String phone;

    @TableField("address")
    private String address;

    /**
     * '过敏史'
     */
    @TableField("allergens")
    private String allergens;

    @TableField("birthday")
    private LocalDateTime birthday;
}
