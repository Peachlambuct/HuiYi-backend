package com.beibei.service.impl;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beibei.entity.dto.SystemBackup;
import com.beibei.mapper.SystemBackupMapper;
import com.beibei.service.SystemBackupService;
import com.beibei.service.SystemNotificationService;
import com.beibei.service.SystemSettingsService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统备份服务实现类
 */
@Slf4j
@Service
public class SystemBackupServiceImpl extends ServiceImpl<SystemBackupMapper, SystemBackup>
        implements SystemBackupService {

    @Resource
    private SystemNotificationService notificationService;

    @Resource
    private SystemSettingsService settingsService;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.password:}")
    private String databasePassword;

    // 备份文件存储目录
    @Value("${app.backup.dir:./backups}")
    private String backupDir;

    private static final DateTimeFormatter FILE_NAME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    @Override
    public Long createBackup(String backupType, String description) {
        String timestamp = LocalDateTime.now().format(FILE_NAME_FORMATTER);
        String filename = "backup_" + timestamp + ".sql";

        // 创建备份目录
        File backupDirFile = new File(backupDir);
        if (!backupDirFile.exists()) {
            boolean created = backupDirFile.mkdirs();
            if (!created) {
                log.error("无法创建备份目录: {}", backupDir);
                throw new RuntimeException("创建备份目录失败");
            }
        }

        String backupFilePath = backupDir + File.separator + filename;

        try {
            // 解析数据库名从URL
            String dbName = extractDatabaseName(datasourceUrl);

            // 执行备份（这里使用示例命令，实际需要根据数据库类型调整）
            // 注意：实际情况需要处理密码安全问题，可能需要使用额外的配置文件
            String command = String.format("mysqldump -u root -p %s --databases %s > %s", "123456", dbName,
                    backupFilePath);

            // 备份数据库
            log.info("执行数据库备份命令: {}", command);

            ProcessBuilder processBuilder = new ProcessBuilder();
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                processBuilder.command("cmd", "/c",
                        "mysqldump", "-u", "root", "-p" + databasePassword,
                        "--databases", dbName, ">", backupFilePath);
            } else {
                processBuilder.command("sh", "-c",
                        "mysqldump -u root -p" + databasePassword + " --databases " + dbName + " > " + backupFilePath);
            }
            Process process = processBuilder.start();

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                // 备份成功，创建备份记录
                File backupFile = new File(backupFilePath);
                long fileSize = backupFile.length();

                SystemBackup backup = new SystemBackup();
                backup.setFilename(filename);
                backup.setFilePath(backupFilePath);
                backup.setFileSize(fileSize);
                backup.setBackupType(backupType);
                backup.setStatus("COMPLETED");
                backup.setDescription(description);
                backup.setCreatedAt(LocalDateTime.now());
                backup.setUpdatedAt(LocalDateTime.now());

                save(backup);

                notificationService.addNotification(
                        "系统备份完成",
                        "系统数据已成功备份，文件: " + filename,
                        "INFO");

                log.info("系统备份完成: {}", filename);
                return backup.getId();
            } else {
                // 备份失败
                throw new RuntimeException("备份命令执行失败，退出码: " + exitCode);
            }
        } catch (Exception e) {
            log.error("创建系统备份时发生错误", e);

            // 创建失败的备份记录
            SystemBackup backup = new SystemBackup();
            backup.setFilename(filename);
            backup.setBackupType(backupType);
            backup.setStatus("FAILED");
            backup.setDescription("备份失败: " + e.getMessage());
            backup.setCreatedAt(LocalDateTime.now());
            backup.setUpdatedAt(LocalDateTime.now());

            save(backup);

            notificationService.addNotification(
                    "系统备份失败",
                    "尝试创建系统备份时发生错误: " + e.getMessage(),
                    "ERROR");

            throw new RuntimeException("创建备份失败", e);
        }
    }

    @Override
    public void restoreBackup(Long backupId) {
        SystemBackup backup = getById(backupId);
        if (backup == null) {
            throw new RuntimeException("备份记录不存在");
        }

        if (!"COMPLETED".equals(backup.getStatus())) {
            throw new RuntimeException("只能恢复成功完成的备份");
        }

        File backupFile = new File(backup.getFilePath());
        if (!backupFile.exists() || !backupFile.isFile()) {
            throw new RuntimeException("备份文件不存在: " + backup.getFilePath());
        }

        try {
            // 恢复前先创建当前状态的备份作为保险
            String safetyDescription = "恢复操作前的自动安全备份 - 恢复ID: " + backupId;
            Long safetyBackupId = createBackup("AUTO_SAFETY", safetyDescription);
            log.info("已创建安全备份，ID: {}", safetyBackupId);

            // 解析数据库名从URL
            String dbName = extractDatabaseName(datasourceUrl);

            // 执行恢复命令
            log.info("开始从备份ID: {} 恢复数据库", backupId);

            ProcessBuilder processBuilder = new ProcessBuilder();
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                processBuilder.command("cmd", "/c",
                        "mysql", "-u", "root", "-p" + databasePassword,
                        dbName, "<", backup.getFilePath());
            } else {
                processBuilder.command("sh", "-c",
                        "mysql -u root -p" + databasePassword + " " + dbName + " < " + backup.getFilePath());
            }
            Process process = processBuilder.start();

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                // 恢复成功
                notificationService.addNotification(
                        "系统恢复完成",
                        "系统已从备份成功恢复: " + backup.getFilename() + "，安全备份ID: " + safetyBackupId,
                        "INFO");

                log.info("系统恢复完成: {}，安全备份ID: {}", backup.getFilename(), safetyBackupId);
            } else {
                // 恢复失败
                throw new RuntimeException("恢复命令执行失败，退出码: " + exitCode + "，但已创建安全备份，ID: " + safetyBackupId);
            }
        } catch (Exception e) {
            log.error("恢复系统备份时发生错误", e);

            notificationService.addNotification(
                    "系统恢复失败",
                    "尝试恢复系统备份时发生错误: " + e.getMessage(),
                    "ERROR");

            throw new RuntimeException("恢复备份失败", e);
        }
    }

    @Override
    @Scheduled(cron = "${app.backup.cron:0 0 2 * * ?}") // 默认每天凌晨2点执行
    public void executeAutoBackup() {
        // 检查是否开启了自动备份
        String autoBackup = settingsService.getSetting("backup.auto_backup", "true");

        if ("true".equalsIgnoreCase(autoBackup)) {
            try {
                String description = "自动定时备份 - " + LocalDate.now();
                createBackup("AUTO", description);
                log.info("自动备份执行完成");
            } catch (Exception e) {
                log.error("执行自动备份时发生错误", e);
            }
        } else {
            log.info("自动备份已禁用，跳过");
        }
    }

    @Override
    @Scheduled(cron = "0 0 3 * * ?") // 每天凌晨3点执行
    public void cleanupOldBackups() {
        try {
            // 获取保留天数设置
            int retentionDays = Integer.parseInt(settingsService.getSetting("backup.retention_days", "30"));

            // 计算截止日期
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retentionDays);

            // 查询过期的备份
            QueryWrapper<SystemBackup> wrapper = new QueryWrapper<>();
            wrapper.lt("created_at", cutoffDate)
                    .eq("backup_type", "AUTO"); // 只自动清理自动备份

            List<SystemBackup> oldBackups = list(wrapper);

            for (SystemBackup backup : oldBackups) {
                // 删除物理文件
                File backupFile = new File(backup.getFilePath());
                if (backupFile.exists()) {
                    boolean deleted = backupFile.delete();
                    if (!deleted) {
                        log.warn("无法删除备份文件: {}", backup.getFilePath());
                    }
                }

                // 删除记录
                removeById(backup.getId());
            }

            if (!oldBackups.isEmpty()) {
                log.info("已清理 {} 个过期备份文件", oldBackups.size());

                notificationService.addNotification(
                        "清理过期备份",
                        "已清理 " + oldBackups.size() + " 个超过 " + retentionDays + " 天的自动备份",
                        "INFO");
            }
        } catch (Exception e) {
            log.error("清理过期备份时发生错误", e);
        }
    }

    @Override
    public Page<SystemBackup> queryBackups(Integer current, Integer size) {
        QueryWrapper<SystemBackup> wrapper = new QueryWrapper<>();
        wrapper.isNull("deleted_at")
                .orderByDesc("created_at");

        return page(new Page<>(current, size), wrapper);
    }

    /**
     * 从数据库URL中提取数据库名
     */
    private String extractDatabaseName(String url) {
        // 示例URL: jdbc:mysql://localhost:3306/huiyi_health
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex >= 0 && lastSlashIndex < url.length() - 1) {
            // 处理URL尾部可能包含的参数
            String dbNameWithParams = url.substring(lastSlashIndex + 1);
            int paramIndex = dbNameWithParams.indexOf('?');
            if (paramIndex > 0) {
                return dbNameWithParams.substring(0, paramIndex);
            }
            return dbNameWithParams;
        }
        throw new IllegalArgumentException("无法从URL解析数据库名: " + url);
    }
}
