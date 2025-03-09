package com.beibei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beibei.entity.dto.SystemSettings;
import com.beibei.entity.vo.request.UpdateSystemSettingsVO;
import com.beibei.entity.vo.response.SystemSettingsVO;
import com.beibei.mapper.SystemSettingsMapper;
import com.beibei.service.AppointmentsService;
import com.beibei.service.CasesService;
import com.beibei.service.PatientsService;
import com.beibei.service.SystemNotificationService;
import com.beibei.service.SystemSettingsService;
import com.beibei.service.UsersService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统设置服务实现类
 */
@Slf4j
@Service
public class SystemSettingsServiceImpl extends ServiceImpl<SystemSettingsMapper, SystemSettings>
        implements SystemSettingsService {

    @Lazy
    @Resource
    private SystemNotificationService notificationService;

    @Resource
    private UsersService usersService;

    @Resource
    private PatientsService patientsService;

    @Resource
    private AppointmentsService appointmentsService;

    @Resource
    private CasesService casesService;

    @PostConstruct
    public void init() {
        // 系统启动时初始化默认设置
        initDefaultSettings();
    }

    @Override
    public SystemSettingsVO getAllSettings() {
        SystemSettingsVO vo = new SystemSettingsVO();

        vo.setBasicSettings(getSettingsByGroup("basic"));
        vo.setBackupSettings(getSettingsByGroup("backup"));
        vo.setEmailSettings(getSettingsByGroup("email"));
        vo.setSecuritySettings(getSettingsByGroup("security"));

        return vo;
    }

    @Override
    public Map<String, String> getSettingsByGroup(String groupName) {
        QueryWrapper<SystemSettings> wrapper = new QueryWrapper<>();
        wrapper.eq("group_name", groupName)
                .isNull("deleted_at");

        List<SystemSettings> settings = list(wrapper);

        return settings.stream()
                .collect(Collectors.toMap(SystemSettings::getSettingKey, SystemSettings::getSettingValue));
    }

    @Override
    public void updateSettings(UpdateSystemSettingsVO updateVO) {
        String groupName = updateVO.getGroupName();
        Map<String, String> settings = updateVO.getSettings();

        if (settings == null || settings.isEmpty()) {
            return;
        }

        for (Map.Entry<String, String> entry : settings.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // 查找是否存在该设置
            QueryWrapper<SystemSettings> wrapper = new QueryWrapper<>();
            wrapper.eq("setting_key", key)
                    .eq("group_name", groupName)
                    .isNull("deleted_at");

            SystemSettings existingSetting = getOne(wrapper);

            if (existingSetting != null) {
                // 更新设置
                existingSetting.setSettingValue(value);
                existingSetting.setUpdatedAt(LocalDateTime.now());
                updateById(existingSetting);
            } else {
                // 创建新设置
                SystemSettings newSetting = new SystemSettings();
                newSetting.setSettingKey(key);
                newSetting.setSettingValue(value);
                newSetting.setGroupName(groupName);
                newSetting.setDescription("用户配置");
                newSetting.setCreatedAt(LocalDateTime.now());
                newSetting.setUpdatedAt(LocalDateTime.now());
                save(newSetting);
            }
        }

        // 记录系统通知
        notificationService.addNotification(
                "系统设置已更新",
                groupName + "组的设置已被更新",
                "INFO");
    }

    @Override
    public String getSetting(String key, String defaultValue) {
        QueryWrapper<SystemSettings> wrapper = new QueryWrapper<>();
        wrapper.eq("setting_key", key)
                .isNull("deleted_at");

        SystemSettings setting = getOne(wrapper);

        if (setting != null) {
            return setting.getSettingValue();
        }

        return defaultValue;
    }

    @Override
    public void initDefaultSettings() {
        // 判断是否已初始化
        Long count = count();

        if (count > 0) {
            log.info("系统设置已存在，跳过初始化");
            return;
        }

        log.info("初始化默认系统设置");

        // 基本设置
        saveDefaultSetting("basic", "site_name", "智慧医疗系统", "站点名称");
        saveDefaultSetting("basic", "log_level", "INFO", "日志级别 (支持: DEBUG, INFO, WARNING, ERROR)");
        saveDefaultSetting("basic", "allow_register", "true", "允许用户注册");
        saveDefaultSetting("basic", "maintenance_mode", "false", "维护模式");

        // 备份设置
        saveDefaultSetting("backup", "auto_backup", "true", "自动备份");
        saveDefaultSetting("backup", "backup_frequency", "daily", "备份频率");
        saveDefaultSetting("backup", "backup_time", "02:00", "备份时间");
        saveDefaultSetting("backup", "retention_days", "30", "保留天数");

        // 邮件设置
        saveDefaultSetting("email", "smtp_server", "", "SMTP服务器");
        saveDefaultSetting("email", "smtp_port", "25", "SMTP端口");
        saveDefaultSetting("email", "smtp_username", "", "SMTP用户名");
        saveDefaultSetting("email", "smtp_password", "", "SMTP密码");
        saveDefaultSetting("email", "mail_from", "", "发件人地址");
        saveDefaultSetting("email", "notification_email", "", "通知邮箱");

        // 安全设置
        saveDefaultSetting("security", "system.cpu.threshold", "80", "CPU使用率阈值");
        saveDefaultSetting("security", "system.memory.threshold", "80", "内存使用率阈值");
        saveDefaultSetting("security", "system.disk.threshold", "80", "磁盘使用率阈值");

        notificationService.addNotification(
                "系统设置已初始化",
                "默认系统设置已成功初始化",
                "INFO");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetSystemData() {
        try {
            // 删除所有数据
            appointmentsService.getBaseMapper().delete(null);
            casesService.getBaseMapper().delete(null);
            patientsService.getBaseMapper().delete(null);

            // 保留管理员账号，删除其他用户
            usersService.removeNonAdminUsers();

            notificationService.addNotification(
                    "系统数据已重置",
                    "所有系统数据已被清除，包括用户、预约和医疗记录",
                    "WARNING");

            log.info("系统数据已重置");
        } catch (Exception e) {
            log.error("重置系统数据时发生错误", e);
            throw e;
        }
    }

    /**
     * 保存默认设置
     */
    private void saveDefaultSetting(String groupName, String key, String value, String description) {
        SystemSettings setting = new SystemSettings();
        setting.setGroupName(groupName);
        setting.setSettingKey(key);
        setting.setSettingValue(value);
        setting.setDescription(description);
        setting.setCreatedAt(LocalDateTime.now());
        setting.setUpdatedAt(LocalDateTime.now());
        save(setting);
    }
}
