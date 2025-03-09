package com.beibei.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.beibei.entity.dto.SystemNotification;
import com.beibei.entity.vo.request.NotificationQueryVO;
import com.beibei.entity.vo.response.SystemNotificationVO;

import java.util.List;

/**
 * 系统通知服务接口
 */
public interface SystemNotificationService extends IService<SystemNotification> {

    /**
     * 添加系统通知
     *
     * @param title   标题
     * @param content 内容
     * @param type    类型
     */
    void addNotification(String title, String content, String type);

    /**
     * 分页查询通知
     *
     * @param queryVO 查询参数
     * @return 通知分页数据
     */
    Page<SystemNotificationVO> queryNotifications(NotificationQueryVO queryVO);

    /**
     * 将通知标记为已读
     *
     * @param id 通知ID
     */
    void markAsRead(Long id);

    /**
     * 获取未读通知数量
     *
     * @return 未读通知数量
     */
    Integer getUnreadCount();

    /**
     * 获取最新的几条通知
     *
     * @param limit 数量限制
     * @return 通知列表
     */
    List<SystemNotificationVO> getLatestNotifications(Integer limit);
}
