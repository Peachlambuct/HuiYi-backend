package com.beibei.service.impl;

import com.beibei.entity.vo.response.SystemStatusVO;
import com.beibei.service.SystemMonitorService;
import com.beibei.service.SystemNotificationService;
import com.beibei.service.SystemSettingsService;
import com.sun.management.OperatingSystemMXBean;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 系统监控服务实现类
 */
@Slf4j
@Service
public class SystemMonitorServiceImpl implements SystemMonitorService {

    @Resource
    private SystemNotificationService notificationService;

    @Resource
    private SystemSettingsService settingsService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    // 默认阈值
    private static final double DEFAULT_CPU_THRESHOLD = 80.0;
    private static final double DEFAULT_MEMORY_THRESHOLD = 80.0;
    private static final double DEFAULT_DISK_THRESHOLD = 80.0;

    @Override
    public SystemStatusVO getSystemStatus() {
        SystemStatusVO status = new SystemStatusVO();

        // 获取操作系统MXBean
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        // CPU使用率
        double cpuUsage = getCpuUsage();
        status.setCpuUsage(roundToTwoDecimal(cpuUsage));

        // 内存使用情况
        long totalMemory = osBean.getTotalMemorySize() / (1024 * 1024);
        long freeMemory = osBean.getFreeMemorySize() / (1024 * 1024);
        long usedMemory = totalMemory - freeMemory;
        double memoryUsage = (double) usedMemory / totalMemory * 100;

        status.setTotalMemory((double) totalMemory);
        status.setUsedMemory((double) usedMemory);
        status.setMemoryUsage(roundToTwoDecimal(memoryUsage));

        // 磁盘使用情况
        File file = new File("/");
        long totalDiskSpace = file.getTotalSpace() / (1024 * 1024 * 1024);
        long freeDiskSpace = file.getFreeSpace() / (1024 * 1024 * 1024);
        long usedDiskSpace = totalDiskSpace - freeDiskSpace;
        double diskUsage = (double) usedDiskSpace / totalDiskSpace * 100;

        status.setTotalDiskSpace((double) totalDiskSpace);
        status.setUsedDiskSpace((double) usedDiskSpace);
        status.setDiskUsage(roundToTwoDecimal(diskUsage));

        // 系统信息
        status.setUptime(ManagementFactory.getRuntimeMXBean().getUptime() / 1000);
        status.setOsName(System.getProperty("os.name") + " " + System.getProperty("os.version"));
        status.setJavaVersion(System.getProperty("java.version"));
        status.setCurrentTime(LocalDateTime.now().format(FORMATTER));

        return status;
    }

    @Override
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void checkSystemResources() {
        try {
            double cpuUsage = getCpuUsage();
            double memoryUsage = getMemoryUsage();
            double diskUsage = getDiskUsage();

            // 获取设置的阈值，如果未设置则使用默认值
            double cpuThreshold = Double.parseDouble(
                    settingsService.getSetting("system.cpu.threshold", String.valueOf(DEFAULT_CPU_THRESHOLD)));
            double memoryThreshold = Double.parseDouble(
                    settingsService.getSetting("system.memory.threshold", String.valueOf(DEFAULT_MEMORY_THRESHOLD)));
            double diskThreshold = Double.parseDouble(
                    settingsService.getSetting("system.disk.threshold", String.valueOf(DEFAULT_DISK_THRESHOLD)));

            // 检查资源使用率是否超过阈值
            if (cpuUsage > cpuThreshold) {
                notificationService.addNotification(
                        "CPU使用率过高",
                        "当前CPU使用率: " + DECIMAL_FORMAT.format(cpuUsage) + "%, 超过设定阈值: " + cpuThreshold + "%",
                        "WARNING");
            }

            if (memoryUsage > memoryThreshold) {
                notificationService.addNotification(
                        "内存使用率过高",
                        "当前内存使用率: " + DECIMAL_FORMAT.format(memoryUsage) + "%, 超过设定阈值: " + memoryThreshold + "%",
                        "WARNING");
            }

            if (diskUsage > diskThreshold) {
                notificationService.addNotification(
                        "磁盘使用率过高",
                        "当前磁盘使用率: " + DECIMAL_FORMAT.format(diskUsage) + "%, 超过设定阈值: " + diskThreshold + "%",
                        "WARNING");
            }
        } catch (Exception e) {
            log.error("检查系统资源时发生错误", e);
        }
    }

    @Override
    public double getCpuUsage() {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return osBean.getCpuLoad() * 100;
    }

    @Override
    public double getMemoryUsage() {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long totalMemory = osBean.getTotalMemorySize();
        long freeMemory = osBean.getFreeMemorySize();
        long usedMemory = totalMemory - freeMemory;
        return (double) usedMemory / totalMemory * 100;
    }

    @Override
    public double getDiskUsage() {
        File file = new File("/");
        long totalSpace = file.getTotalSpace();
        long freeSpace = file.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;
        return (double) usedSpace / totalSpace * 100;
    }

    /**
     * 将double值四舍五入到两位小数
     */
    private double roundToTwoDecimal(double value) {
        return Double.parseDouble(DECIMAL_FORMAT.format(value));
    }
}
