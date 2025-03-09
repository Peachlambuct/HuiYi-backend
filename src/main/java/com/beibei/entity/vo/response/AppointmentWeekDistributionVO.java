package com.beibei.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 每周预约分布数据VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentWeekDistributionVO {
    /**
     * 周一至周日的预约数量
     */
    private List<Integer> data;

    /**
     * 横坐标标签
     */
    private List<String> labels;
}
