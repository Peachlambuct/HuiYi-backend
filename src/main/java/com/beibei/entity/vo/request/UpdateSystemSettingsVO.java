package com.beibei.entity.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 更新系统设置请求VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSystemSettingsVO {

    /**
     * 设置组名
     */
    private String groupName;

    /**
     * 设置键值对
     */
    private Map<String, String> settings;
}
