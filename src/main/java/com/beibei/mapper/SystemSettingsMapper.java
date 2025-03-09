package com.beibei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.beibei.entity.dto.SystemSettings;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 系统设置Mapper接口
 */
@Mapper
public interface SystemSettingsMapper extends BaseMapper<SystemSettings> {

    /**
     * 按组名获取系统设置
     */
    @Select("SELECT setting_key, setting_value FROM system_settings WHERE group_name = #{groupName} AND deleted_at IS NULL")
    List<Map<String, String>> getSettingsByGroup(String groupName);
}
