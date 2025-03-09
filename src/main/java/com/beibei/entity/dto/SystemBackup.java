package com.beibei.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 系统备份实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("system_backups")
public class SystemBackup {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 备份文件名
     */
    private String filename;

    /**
     * 备份文件路径
     */
    private String filePath;

    /**
     * 备份大小 (字节)
     */
    private Long fileSize;

    /**
     * 备份类型: AUTO, MANUAL
     */
    private String backupType;

    /**
     * 备份状态: COMPLETED, FAILED
     */
    private String status;

    /**
     * 备份描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 最后更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 删除时间
     */
    private LocalDateTime deletedAt;
}
