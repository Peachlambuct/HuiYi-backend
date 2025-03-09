package com.beibei.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 患者统计数据VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientStatsVO {
    /**
     * 总患者数
     */
    private Integer total;

    /**
     * 今日新增
     */
    private Integer todayNew;

    /**
     * 本月活跃
     */
    private Integer monthlyActive;
}
