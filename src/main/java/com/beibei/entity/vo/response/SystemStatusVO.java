package com.beibei.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统状态VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemStatusVO {

    /**
     * CPU 使用率 (%)
     */
    private Double cpuUsage;

    /**
     * 内存使用率 (%)
     */
    private Double memoryUsage;

    /**
     * 已用内存 (MB)
     */
    private Double usedMemory;

    /**
     * 总内存 (MB)
     */
    private Double totalMemory;

    /**
     * 磁盘使用率 (%)
     */
    private Double diskUsage;

    /**
     * 已用磁盘空间 (GB)
     */
    private Double usedDiskSpace;

    /**
     * 总磁盘空间 (GB)
     */
    private Double totalDiskSpace;

    /**
     * 系统运行时间 (秒)
     */
    private Long uptime;

    /**
     * 操作系统名称及版本
     */
    private String osName;

    /**
     * Java 版本
     */
    private String javaVersion;

    /**
     * 当前时间
     */
    private String currentTime;
}
