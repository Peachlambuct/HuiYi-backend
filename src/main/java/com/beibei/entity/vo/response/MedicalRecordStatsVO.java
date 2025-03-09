package com.beibei.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 医疗记录统计数据VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordStatsVO {
    /**
     * 总记录数
     */
    private Integer total;

    /**
     * 今日新增
     */
    private Integer todayNew;

    /**
     * 总预约数
     */
    private Integer totalAppointments;
}
