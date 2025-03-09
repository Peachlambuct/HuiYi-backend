package com.beibei.entity.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通知分页查询VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationQueryVO {

    /**
     * 当前页
     */
    private Integer current = 1;

    /**
     * 每页大小
     */
    private Integer size = 10;

    /**
     * 类型筛选
     */
    private String type;

    /**
     * 阅读状态筛选
     */
    private Boolean isRead;
}
