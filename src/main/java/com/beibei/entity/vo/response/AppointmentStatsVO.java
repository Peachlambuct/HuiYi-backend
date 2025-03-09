package com.beibei.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 今日预约统计数据VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentStatsVO {
    /**
     * 总预约数
     */
    private Integer total;

    /**
     * 待完成数量
     */
    private Integer pending;

    /**
     * 已完成数量
     */
    private Integer completed;
}
