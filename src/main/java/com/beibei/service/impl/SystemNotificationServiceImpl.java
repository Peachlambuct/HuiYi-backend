package com.beibei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beibei.entity.dto.SystemNotification;
import com.beibei.entity.vo.request.NotificationQueryVO;
import com.beibei.entity.vo.response.SystemNotificationVO;
import com.beibei.mapper.SystemNotificationMapper;
import com.beibei.service.SystemNotificationService;
import com.beibei.service.SystemSettingsService;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统通知服务实现类
 */
@Slf4j
@Service
public class SystemNotificationServiceImpl extends ServiceImpl<SystemNotificationMapper, SystemNotification>
        implements SystemNotificationService, ApplicationListener<ContextRefreshedEvent> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Resource
    private SystemSettingsService settingsService;

    private boolean applicationStarted = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        applicationStarted = true;
    }

    @Override
    public void addNotification(String title, String content, String type) {
        // 如果应用程序尚未完全启动，则不检查日志级别过滤
        // 这避免了在初始化阶段的循环依赖问题
        if (!applicationStarted) {
            SystemNotification notification = new SystemNotification();
            notification.setTitle(title);
            notification.setContent(content);
            notification.setType(type);
            notification.setIsRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setUpdatedAt(LocalDateTime.now());

            save(notification);
            log.debug("初始化阶段添加通知: {} - {} [{}]", title, content, type);
            return;
        }

        // 检查日志级别设置
        String configuredLogLevel = settingsService.getSetting("log_level", "INFO");

        // 只有当通知类型大于等于配置的日志级别时才添加
        if (isLevelAllowed(type, configuredLogLevel)) {
            SystemNotification notification = new SystemNotification();
            notification.setTitle(title);
            notification.setContent(content);
            notification.setType(type);
            notification.setIsRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setUpdatedAt(LocalDateTime.now());

            save(notification);
            log.debug("添加通知: {} - {} [{}]", title, content, type);
        } else {
            log.debug("忽略低级别通知: {} - {} [{}]，配置级别为: {}", title, content, type, configuredLogLevel);
        }
    }

    /**
     * 判断通知级别是否允许被记录
     *
     * @param notificationType 通知类型
     * @param configuredLevel  配置的级别
     * @return 是否允许
     */
    private boolean isLevelAllowed(String notificationType, String configuredLevel) {
        // 定义日志级别顺序：DEBUG < INFO < WARNING < ERROR
        int typeLevel = getLevelValue(notificationType);
        int configLevel = getLevelValue(configuredLevel);

        // 只有大于等于配置级别的通知才被允许
        return typeLevel >= configLevel;
    }

    /**
     * 获取日志级别的数值表示
     *
     * @param level 日志级别
     * @return 数值大小
     */
    private int getLevelValue(String level) {
        if (level == null) {
            return 0;
        }

        switch (level.toUpperCase()) {
            case "DEBUG":
                return 1;
            case "INFO":
                return 2;
            case "WARNING":
                return 3;
            case "ERROR":
                return 4;
            default:
                return 0;
        }
    }

    @Override
    public Page<SystemNotificationVO> queryNotifications(NotificationQueryVO queryVO) {
        QueryWrapper<SystemNotification> wrapper = new QueryWrapper<>();
        wrapper.isNull("deleted_at");

        // 添加过滤条件
        if (queryVO.getType() != null && !queryVO.getType().isEmpty()) {
            wrapper.eq("type", queryVO.getType());
        }

        if (queryVO.getIsRead() != null) {
            wrapper.eq("is_read", queryVO.getIsRead());
        }

        // 应用程序完全启动后才应用日志级别过滤
        if (applicationStarted) {
            // 获取系统配置的日志级别并添加过滤
            String configuredLogLevel = settingsService.getSetting("log_level", "INFO");
            int configLevelValue = getLevelValue(configuredLogLevel);

            // 只显示大于等于配置级别的通知
            wrapper.exists("SELECT 1 FROM dual WHERE CASE type " +
                    "WHEN 'DEBUG' THEN 1 " +
                    "WHEN 'INFO' THEN 2 " +
                    "WHEN 'WARNING' THEN 3 " +
                    "WHEN 'ERROR' THEN 4 " +
                    "ELSE 0 END >= " + configLevelValue);
        }

        // 按创建时间倒序排序
        wrapper.orderByDesc("created_at");

        // 执行分页查询
        Page<SystemNotification> page = new Page<>(queryVO.getCurrent(), queryVO.getSize());
        Page<SystemNotification> resultPage = page(page, wrapper);

        // 转换为VO
        Page<SystemNotificationVO> voPage = new Page<>();
        voPage.setCurrent(resultPage.getCurrent());
        voPage.setSize(resultPage.getSize());
        voPage.setTotal(resultPage.getTotal());
        voPage.setPages(resultPage.getPages());

        List<SystemNotificationVO> records = resultPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        voPage.setRecords(records);

        return voPage;
    }

    @Override
    public void markAsRead(Long id) {
        SystemNotification notification = getById(id);
        if (notification != null && !notification.getIsRead()) {
            notification.setIsRead(true);
            notification.setUpdatedAt(LocalDateTime.now());
            updateById(notification);
        }
    }

    @Override
    public Integer getUnreadCount() {
        QueryWrapper<SystemNotification> wrapper = new QueryWrapper<>();
        wrapper.eq("is_read", false)
                .isNull("deleted_at");

        // 应用程序完全启动后才应用日志级别过滤
        if (applicationStarted) {
            // 添加日志级别过滤
            String configuredLogLevel = settingsService.getSetting("log_level", "INFO");
            int configLevelValue = getLevelValue(configuredLogLevel);

            wrapper.exists("SELECT 1 FROM dual WHERE CASE type " +
                    "WHEN 'DEBUG' THEN 1 " +
                    "WHEN 'INFO' THEN 2 " +
                    "WHEN 'WARNING' THEN 3 " +
                    "WHEN 'ERROR' THEN 4 " +
                    "ELSE 0 END >= " + configLevelValue);
        }

        return Math.toIntExact(count(wrapper));
    }

    @Override
    public List<SystemNotificationVO> getLatestNotifications(Integer limit) {
        QueryWrapper<SystemNotification> wrapper = new QueryWrapper<>();
        wrapper.isNull("deleted_at")
                .orderByDesc("created_at");

        // 应用程序完全启动后才应用日志级别过滤
        if (applicationStarted) {
            // 添加日志级别过滤
            String configuredLogLevel = settingsService.getSetting("log_level", "INFO");
            int configLevelValue = getLevelValue(configuredLogLevel);

            wrapper.exists("SELECT 1 FROM dual WHERE CASE type " +
                    "WHEN 'DEBUG' THEN 1 " +
                    "WHEN 'INFO' THEN 2 " +
                    "WHEN 'WARNING' THEN 3 " +
                    "WHEN 'ERROR' THEN 4 " +
                    "ELSE 0 END >= " + configLevelValue);
        }

        Page<SystemNotification> page = new Page<>(1, limit);
        Page<SystemNotification> resultPage = page(page, wrapper);

        return resultPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 将实体转换为VO
     */
    private SystemNotificationVO convertToVO(SystemNotification notification) {
        SystemNotificationVO vo = new SystemNotificationVO();
        vo.setId(notification.getId());
        vo.setTitle(notification.getTitle());
        vo.setContent(notification.getContent());
        vo.setType(notification.getType());
        vo.setIsRead(notification.getIsRead());

        if (notification.getCreatedAt() != null) {
            vo.setCreatedAt(notification.getCreatedAt().format(FORMATTER));
        }

        return vo;
    }
}
