package com.beibei.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统通知VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemNotificationVO {

    /**
     * ID
     */
    private Long id;

    /**
     * 提醒标题
     */
    private String title;

    /**
     * 提醒内容
     */
    private String content;

    /**
     * 提醒类型
     */
    private String type;

    /**
     * 是否已读
     */
    private Boolean isRead;

    /**
     * 创建时间
     */
    private String createdAt;
}
