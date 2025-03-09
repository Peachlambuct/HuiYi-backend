package com.beibei.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 系统设置实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("system_settings")
public class SystemSettings {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 设置键
     */
    private String settingKey;

    /**
     * 设置值
     */
    private String settingValue;

    /**
     * 设置描述
     */
    private String description;

    /**
     * 分组
     */
    private String groupName;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 最后更新时间
     */
    private LocalDateTime updatedAt;
}
