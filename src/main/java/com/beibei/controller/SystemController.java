package com.beibei.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beibei.entity.RestBean;
import com.beibei.entity.dto.SystemBackup;
import com.beibei.entity.vo.request.NotificationQueryVO;
import com.beibei.entity.vo.request.UpdateSystemSettingsVO;
import com.beibei.entity.vo.response.SystemNotificationVO;
import com.beibei.entity.vo.response.SystemSettingsVO;
import com.beibei.entity.vo.response.SystemStatusVO;
import com.beibei.service.SystemBackupService;
import com.beibei.service.SystemMonitorService;
import com.beibei.service.SystemNotificationService;
import com.beibei.service.SystemSettingsService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/system")
public class SystemController {

    @Resource
    private SystemMonitorService monitorService;

    @Resource
    private SystemNotificationService notificationService;

    @Resource
    private SystemSettingsService settingsService;

    @Resource
    private SystemBackupService backupService;

    /**
     * 获取系统状态
     */
    @GetMapping("/status")
    @PreAuthorize("hasRole('admin')")
    public RestBean<SystemStatusVO> getSystemStatus() {
        return RestBean.success(monitorService.getSystemStatus());
    }

    /**
     * 获取系统通知列表
     */
    @PostMapping("/notifications")
    @PreAuthorize("hasRole('admin')")
    public RestBean<Page<SystemNotificationVO>> getNotifications(@RequestBody NotificationQueryVO queryVO) {
        return RestBean.success(notificationService.queryNotifications(queryVO));
    }

    /**
     * 获取未读通知数量
     */
    @GetMapping("/notifications/unread-count")
    @PreAuthorize("hasRole('admin')")
    public RestBean<Integer> getUnreadNotificationCount() {
        return RestBean.success(notificationService.getUnreadCount());
    }

    /**
     * 获取最新的通知
     */
    @GetMapping("/notifications/latest")
    @PreAuthorize("hasRole('admin')")
    public RestBean<List<SystemNotificationVO>> getLatestNotifications(
            @RequestParam(defaultValue = "5") Integer limit) {
        return RestBean.success(notificationService.getLatestNotifications(limit));
    }

    /**
     * 将通知标记为已读
     */
    @GetMapping("/notifications/mark-read")
    @PreAuthorize("hasRole('admin')")
    public RestBean<Void> markNotificationAsRead(@RequestParam Long id) {
        notificationService.markAsRead(id);
        return RestBean.success();
    }

    /**
     * 获取系统设置
     */
    @GetMapping("/settings")
    @PreAuthorize("hasRole('admin')")
    public RestBean<SystemSettingsVO> getSystemSettings() {
        return RestBean.success(settingsService.getAllSettings());
    }

    /**
     * 更新系统设置
     */
    @PostMapping("/settings/update")
    @PreAuthorize("hasRole('admin')")
    public RestBean<Void> updateSystemSettings(@RequestBody UpdateSystemSettingsVO updateVO) {
        settingsService.updateSettings(updateVO);
        return RestBean.success();
    }

    /**
     * 重置系统数据
     */
    @GetMapping("/reset")
    @PreAuthorize("hasRole('admin')")
    public RestBean<Void> resetSystemData() {
        settingsService.resetSystemData();
        return RestBean.success();
    }

    /**
     * 创建系统备份
     */
    @GetMapping("/backup/create")
    @PreAuthorize("hasRole('admin')")
    public RestBean<Void> createBackup(@RequestParam(required = false) String description) {
        backupService.createBackup("MANUAL", description);
        return RestBean.success();
    }

    /**
     * 恢复系统备份
     */
    @GetMapping("/backup/restore")
    @PreAuthorize("hasRole('admin')")
    public RestBean<Void> restoreBackup(@RequestParam Long id) {
        backupService.restoreBackup(id);
        return RestBean.success();
    }

    /**
     * 获取备份列表
     */
    @GetMapping("/backup/list")
    @PreAuthorize("hasRole('admin')")
    public RestBean<Page<SystemBackup>> getBackups(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        return RestBean.success(backupService.queryBackups(current, size));
    }

    /**
     * 获取是否系统处于维护状态
     */
    @GetMapping("/maintenance")
    public RestBean<Boolean> getMaintenanceStatus() {
        return RestBean.success(Boolean.parseBoolean(settingsService.getSetting("maintenance_mode", "false")));
    }
}
