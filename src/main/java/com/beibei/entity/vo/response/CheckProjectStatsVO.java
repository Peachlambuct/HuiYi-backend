package com.beibei.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 检查项目统计数据VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckProjectStatsVO {
    /**
     * 检查项目总数
     */
    private Integer total;

    /**
     * 最受欢迎的检查项目
     */
    private String mostPopular;
}
