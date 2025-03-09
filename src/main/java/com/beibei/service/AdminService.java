package com.beibei.service;

import com.beibei.entity.dto.CheckProjects;
import com.beibei.entity.vo.request.AdminLoginVO;
import com.beibei.entity.vo.request.UpdateCheckProjectVO;
import com.beibei.entity.vo.response.CountResponseVO;
import com.beibei.entity.vo.response.DoctorCard;
import com.beibei.entity.vo.response.PatientInfoVO;
import com.beibei.entity.vo.response.DashboardDataVO;
import com.beibei.entity.vo.response.AppointmentWeekDistributionVO;
import com.beibei.entity.vo.response.PatientGrowthTrendVO;
import com.beibei.entity.vo.response.AppointmentStatsVO;
import com.beibei.entity.vo.response.PatientStatsVO;
import com.beibei.entity.vo.response.MedicalRecordStatsVO;
import com.beibei.entity.vo.response.CheckProjectStatsVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {
    String getKey();

    boolean login(AdminLoginVO vo);

    void addCheckProject(CheckProjects checkProjects);

    void updateCheckProject(UpdateCheckProjectVO vo);

    void deleteCheckProject(Integer id);

    List<CheckProjects> getCheckProjects();

    CountResponseVO getCount();

    List<DoctorCard> getDoctorInfo();

    List<PatientInfoVO> getPatientInfo();

    void deletePatient(Long userId);

    void deleteDoctor(Long userId);

    /**
     * 获取仪表盘数据
     */
    DashboardDataVO getDashboardData();

    /**
     * 获取今日预约统计数据
     */
    AppointmentStatsVO getAppointmentStats();

    /**
     * 获取患者统计数据
     */
    PatientStatsVO getPatientStats();

    /**
     * 获取医疗记录统计数据
     */
    MedicalRecordStatsVO getMedicalRecordStats();

    /**
     * 获取检查项目统计数据
     */
    CheckProjectStatsVO getCheckProjectStats();

    /**
     * 获取每周预约分布数据
     */
    AppointmentWeekDistributionVO getAppointmentWeeklyDistribution();

    /**
     * 获取患者增长趋势数据
     */
    PatientGrowthTrendVO getPatientGrowthTrend();
}
