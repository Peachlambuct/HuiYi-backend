package com.beibei.controller;

import com.beibei.entity.RestBean;
import com.beibei.entity.dto.CheckProjects;
import com.beibei.entity.dto.Users;
import com.beibei.entity.vo.request.AdminLoginVO;
import com.beibei.entity.vo.request.UpdateCheckProjectVO;
import com.beibei.entity.vo.response.AuthorizeVO;
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
import com.beibei.service.AdminService;
import com.beibei.utils.JwtUtils;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Resource
    private AdminService adminService;
    @Resource
    private JwtUtils utils;

    @GetMapping("/getkey")
    public RestBean<String> getKey() {
        return RestBean.success(adminService.getKey());
    }

    @PostMapping("/login")
    public RestBean<AuthorizeVO> login(@RequestBody AdminLoginVO vo) {
        if (!adminService.login(vo)) {
            return RestBean.error(new Exception("登录失败"));
        }
        Users user = new Users();
        user.setRole("admin");
        user.setUsername("admin");
        user.setId(10000L);
        UserDetails admin = User
                .withUsername("admin")
                .password("hakjwhdkjhajkshdkjahwkjdhakjshdkjawhkjd")
                .roles(user.getRole())
                .build();
        String jwt = utils.createJwt(admin, "admin", user.getId());
        if (jwt == null) {
            return RestBean.forbidden("登录验证频繁，请稍后再试");
        } else {
            AuthorizeVO authorizeVO = new AuthorizeVO();
            authorizeVO.setToken(jwt);
            authorizeVO.setExpire(utils.expireTime());
            authorizeVO.setUsername("admin");
            authorizeVO.setStatus(true);
            authorizeVO.setRole("admin");
            authorizeVO.setUserEmail("1479539484@qq.com");
            return RestBean.success(authorizeVO);
        }
    }

    @PostMapping("/add")
    public RestBean<String> add(@RequestBody CheckProjects checkProjects) {
        adminService.addCheckProject(checkProjects);
        return RestBean.success();
    }

    @PostMapping("/update")
    public RestBean<String> update(@RequestBody UpdateCheckProjectVO vo) {
        adminService.updateCheckProject(vo);
        return RestBean.success();
    }

    @GetMapping("/delete")
    public RestBean<String> delete(@RequestParam("id") Integer id) {
        adminService.deleteCheckProject(id);
        return RestBean.success();
    }

    @GetMapping("/info")
    public RestBean<List<CheckProjects>> info() {
        return RestBean.success(adminService.getCheckProjects());
    }

    @GetMapping("/count")
    public RestBean<CountResponseVO> count() {
        return RestBean.success(adminService.getCount());
    }

    @GetMapping("/doctor")
    public RestBean<List<DoctorCard>> doctor() {
        return RestBean.success(adminService.getDoctorInfo());
    }

    @GetMapping("/patient")
    public RestBean<List<PatientInfoVO>> patient() {
        return RestBean.success(adminService.getPatientInfo());
    }

    @GetMapping("/deldoctor")
    public RestBean<Void> delDoctor(@RequestParam("id") Long id) {
        adminService.deleteDoctor(id);
        return RestBean.success();
    }

    @GetMapping("/delpatient")
    public RestBean<Void> delPatient(@RequestParam("id") Long id) {
        adminService.deletePatient(id);
        return RestBean.success();
    }

    /**
     * 获取仪表盘数据
     */
    @GetMapping("/dashboard")
    public RestBean<DashboardDataVO> getDashboardData() {
        return RestBean.success(adminService.getDashboardData());
    }

    /**
     * 获取今日预约统计数据
     */
    @GetMapping("/stats/appointment")
    public RestBean<AppointmentStatsVO> getAppointmentStats() {
        return RestBean.success(adminService.getAppointmentStats());
    }

    /**
     * 获取患者统计数据
     */
    @GetMapping("/stats/patient")
    public RestBean<PatientStatsVO> getPatientStats() {
        return RestBean.success(adminService.getPatientStats());
    }

    /**
     * 获取医疗记录统计数据
     */
    @GetMapping("/stats/medical")
    public RestBean<MedicalRecordStatsVO> getMedicalRecordStats() {
        return RestBean.success(adminService.getMedicalRecordStats());
    }

    /**
     * 获取检查项目统计数据
     */
    @GetMapping("/stats/check")
    public RestBean<CheckProjectStatsVO> getCheckProjectStats() {
        return RestBean.success(adminService.getCheckProjectStats());
    }

    /**
     * 获取每周预约分布数据
     */
    @GetMapping("/stats/appointment/weekly")
    public RestBean<AppointmentWeekDistributionVO> getAppointmentWeeklyDistribution() {
        return RestBean.success(adminService.getAppointmentWeeklyDistribution());
    }

    /**
     * 获取患者增长趋势数据
     */
    @GetMapping("/stats/patient/growth")
    public RestBean<PatientGrowthTrendVO> getPatientGrowthTrend() {
        return RestBean.success(adminService.getPatientGrowthTrend());
    }
}
