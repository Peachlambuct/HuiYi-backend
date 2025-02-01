package com.beibei.entity.dto;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.beibei.entity.BaseData;
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
@TableName("users")
public class Users implements Serializable, BaseData {

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

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("email")
    private String email;

    @TableField("role")
    private String role;

    @TableField("avatar")
    private String avatar;

    @TableField("status")
    private Boolean status;
}
