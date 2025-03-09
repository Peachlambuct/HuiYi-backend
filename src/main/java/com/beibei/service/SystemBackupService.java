package com.beibei.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.beibei.entity.dto.SystemBackup;

/**
 * 系统备份服务接口
 */
public interface SystemBackupService extends IService<SystemBackup> {

    /**
     * 创建系统备份
     *
     * @param backupType  备份类型：AUTO, MANUAL
     * @param description 备份描述
     * @return 备份ID
     */
    Long createBackup(String backupType, String description);

    /**
     * 恢复系统备份
     *
     * @param backupId 备份ID
     */
    void restoreBackup(Long backupId);

    /**
     * 执行自动备份任务
     */
    void executeAutoBackup();

    /**
     * 清理过期备份
     */
    void cleanupOldBackups();

    /**
     * 分页查询备份记录
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 分页数据
     */
    Page<SystemBackup> queryBackups(Integer current, Integer size);
}
