package com.beibei.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 仪表盘统计数据VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDataVO {
    /**
     * 今日预约统计
     */
    private AppointmentStatsVO appointmentStats;

    /**
     * 患者统计
     */
    private PatientStatsVO patientStats;

    /**
     * 医疗记录统计
     */
    private MedicalRecordStatsVO medicalRecordStats;

    /**
     * 检查项目统计
     */
    private CheckProjectStatsVO checkProjectStats;

    /**
     * 每周预约分布
     */
    private AppointmentWeekDistributionVO weeklyDistribution;

    /**
     * 患者增长趋势
     */
    private PatientGrowthTrendVO patientGrowth;
}
