package com.beibei.service;

import com.beibei.entity.vo.response.SystemStatusVO;

/**
 * 系统监控服务接口
 */
public interface SystemMonitorService {

    /**
     * 获取系统状态
     *
     * @return 系统状态数据
     */
    SystemStatusVO getSystemStatus();

    /**
     * 检查系统资源使用情况
     * 如果系统资源使用率超过阈值，则创建通知
     */
    void checkSystemResources();

    /**
     * 获取系统CPU使用率
     *
     * @return CPU使用率
     */
    double getCpuUsage();

    /**
     * 获取系统内存使用率
     *
     * @return 内存使用率
     */
    double getMemoryUsage();

    /**
     * 获取系统磁盘使用率
     *
     * @return 磁盘使用率
     */
    double getDiskUsage();
}
