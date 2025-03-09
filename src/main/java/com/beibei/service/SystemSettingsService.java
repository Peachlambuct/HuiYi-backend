package com.beibei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.beibei.entity.dto.SystemSettings;
import com.beibei.entity.vo.request.UpdateSystemSettingsVO;
import com.beibei.entity.vo.response.SystemSettingsVO;

import java.util.Map;

/**
 * 系统设置服务接口
 */
public interface SystemSettingsService extends IService<SystemSettings> {

    /**
     * 获取所有系统设置
     *
     * @return 系统设置数据
     */
    SystemSettingsVO getAllSettings();

    /**
     * 获取指定组的设置
     *
     * @param groupName 组名
     * @return 设置键值对
     */
    Map<String, String> getSettingsByGroup(String groupName);

    /**
     * 更新系统设置
     *
     * @param updateVO 更新请求
     */
    void updateSettings(UpdateSystemSettingsVO updateVO);

    /**
     * 获取单个设置值
     *
     * @param key          设置键
     * @param defaultValue 默认值
     * @return 设置值
     */
    String getSetting(String key, String defaultValue);

    /**
     * 初始化默认系统设置
     */
    void initDefaultSettings();

    /**
     * 重置系统数据
     */
    void resetSystemData();
}
