package com.beibei.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 系统设置VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemSettingsVO {

    /**
     * 基本设置
     */
    private Map<String, String> basicSettings;

    /**
     * 备份设置
     */
    private Map<String, String> backupSettings;

    /**
     * 邮件设置
     */
    private Map<String, String> emailSettings;

    /**
     * 安全设置
     */
    private Map<String, String> securitySettings;
}
