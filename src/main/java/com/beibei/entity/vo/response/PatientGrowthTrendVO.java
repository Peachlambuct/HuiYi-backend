package com.beibei.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 患者增长趋势数据VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientGrowthTrendVO {
    /**
     * 各月份患者数量
     */
    private List<Integer> data;

    /**
     * 横坐标标签（月份）
     */
    private List<String> labels;
}
