package com.beibei.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@Getter
@Setter
@TableName("patients")
public class Patients implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
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
