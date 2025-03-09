package com.beibei.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beibei.entity.dto.CheckProjects;
import com.beibei.entity.dto.Doctors;
import com.beibei.entity.dto.Patients;
import com.beibei.entity.dto.Users;
import com.beibei.entity.vo.request.AdminLoginVO;
import com.beibei.entity.vo.request.UpdateCheckProjectVO;
import com.beibei.entity.vo.response.AppointmentStatsVO;
import com.beibei.entity.vo.response.AppointmentWeekDistributionVO;
import com.beibei.entity.vo.response.CheckProjectStatsVO;
import com.beibei.entity.vo.response.CountResponseVO;
import com.beibei.entity.vo.response.DashboardDataVO;
import com.beibei.entity.vo.response.DoctorCard;
import com.beibei.entity.vo.response.MedicalRecordStatsVO;
import com.beibei.entity.vo.response.PatientGrowthTrendVO;
import com.beibei.entity.vo.response.PatientInfoVO;
import com.beibei.entity.vo.response.PatientStatsVO;
import com.beibei.mapper.AppointmentsMapper;
import com.beibei.mapper.CasesMapper;
import com.beibei.mapper.CheckProjectsMapper;
import com.beibei.mapper.PatientsMapper;
import com.beibei.service.AdminService;
import com.beibei.service.AppointmentsService;
import com.beibei.service.CasesService;
import com.beibei.service.CheckProjectsService;
import com.beibei.service.DoctorsService;
import com.beibei.service.PatientsService;
import com.beibei.service.UsersService;
import com.beibei.utils.GenerateRandomKeyUtil;

import cn.hutool.core.bean.BeanUtil;
import jakarta.annotation.Resource;

@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    private GenerateRandomKeyUtil generateRandomKeyUtil;
    @Resource
    private CheckProjectsService checkProjectService;
    @Resource
    private PatientsService patientsService;
    @Resource
    private DoctorsService doctorsService;
    @Resource
    private UsersService usersService;
    @Resource
    private AppointmentsService appointmentsService;
    @Resource
    private CasesService casesService;
    @Resource
    private AppointmentsMapper appointmentsMapper;
    @Resource
    private PatientsMapper patientsMapper;
    @Resource
    private CasesMapper casesMapper;
    @Resource
    private CheckProjectsMapper checkProjectsMapper;

    @Override
    public String getKey() {
        return generateRandomKeyUtil.generateAndSaveKey();
    }

    @Override
    public boolean login(AdminLoginVO vo) {
        return "admin".equals(vo.getUsername()) && generateRandomKeyUtil.checkKey(vo.getKey());
    }

    @Override
    public void addCheckProject(CheckProjects checkProjects) {
        checkProjectService.save(checkProjects);
    }

    @Override
    public void updateCheckProject(UpdateCheckProjectVO vo) {
        CheckProjects byId = checkProjectService.getById(vo.getId());
        byId.setName(vo.getName());
        byId.setRoom(vo.getRoom());
        checkProjectService.updateById(byId);
    }

    @Override
    public void deleteCheckProject(Integer id) {
        CheckProjects byId = checkProjectService.getById(id);
        byId.setDeletedAt(LocalDateTime.now());
        checkProjectService.updateById(byId);
    }

    @Override
    public List<CheckProjects> getCheckProjects() {
        return checkProjectService.list(new QueryWrapper<CheckProjects>().isNull("deleted_at"));
    }

    @Override
    public CountResponseVO getCount() {
        long patientCount = patientsService.count();
        long doctorCount = doctorsService.count();
        long checkProjectCount = checkProjectService.count();
        return new CountResponseVO(patientCount, doctorCount, checkProjectCount);
    }

    @Override
    public List<DoctorCard> getDoctorInfo() {
        return doctorsService.findAllDoctors();
    }

    @Override
    public List<PatientInfoVO> getPatientInfo() {
        List<Patients> patients = patientsService.list();
        return patients.stream().map(patient -> {
            PatientInfoVO patientInfoVO = new PatientInfoVO();
            BeanUtil.copyProperties(patient, patientInfoVO);
            patientInfoVO.setSex(patient.getSex() ? "男" : "女");
            if (patient.getBirthday() != null) {
                LocalDate birthDate = patient.getBirthday().toLocalDate();
                int age = LocalDate.now().getYear() - birthDate.getYear();
                if (birthDate.getMonthValue() > LocalDate.now().getMonthValue() ||
                        (birthDate.getMonthValue() == LocalDate.now().getMonthValue() &&
                                birthDate.getDayOfMonth() > LocalDate.now().getDayOfMonth())) {
                    age--;
                }
                patientInfoVO.setAge(age);
            }
            return patientInfoVO;
        }).toList();
    }

    @Override
    public void deletePatient(Long userId) {
        Patients byId = patientsService.getById(userId);
        byId.setDeletedAt(LocalDateTime.now());
        Users users = usersService.getById(byId.getUserId());
        users.setDeletedAt(LocalDateTime.now());
        usersService.updateById(users);
    }

    @Override
    public void deleteDoctor(Long userId) {
        Doctors byId = doctorsService.getById(userId);
        byId.setDeletedAt(LocalDateTime.now());
        Users users = usersService.getById(byId.getUserId());
        users.setDeletedAt(LocalDateTime.now());
        usersService.updateById(users);
    }

    @Override
    public DashboardDataVO getDashboardData() {
        DashboardDataVO dashboardData = new DashboardDataVO();

        // 设置各项统计数据
        dashboardData.setAppointmentStats(getAppointmentStats());
        dashboardData.setPatientStats(getPatientStats());
        dashboardData.setMedicalRecordStats(getMedicalRecordStats());
        dashboardData.setCheckProjectStats(getCheckProjectStats());
        dashboardData.setWeeklyDistribution(getAppointmentWeeklyDistribution());
        dashboardData.setPatientGrowth(getPatientGrowthTrend());

        return dashboardData;
    }

    @Override
    public AppointmentStatsVO getAppointmentStats() {
        // 获取当前日期
        LocalDate today = LocalDate.now();
        String year = String.valueOf(today.getYear());
        String month = String.valueOf(today.getMonthValue());
        String day = String.valueOf(today.getDayOfMonth());

        // 使用Mapper直接查询统计数据
        AppointmentStatsVO stats = appointmentsMapper.getAppointmentStats(year, month, day);

        // 如果结果为null（可能是今天没有预约），则返回空对象
        if (stats == null) {
            return new AppointmentStatsVO(0, 0, 0);
        }
        if (stats.getTotal() == 0) {
            return new AppointmentStatsVO(0, 0, 0);
        }

        return stats;
    }

    @Override
    public PatientStatsVO getPatientStats() {
        // 使用Mapper直接查询统计数据
        PatientStatsVO stats = patientsMapper.getPatientStats();

        // 如果结果为null，则返回空对象
        if (stats == null) {
            return new PatientStatsVO(0, 0, 0);
        }

        return stats;
    }

    @Override
    public MedicalRecordStatsVO getMedicalRecordStats() {
        // 使用Mapper直接查询统计数据
        MedicalRecordStatsVO stats = casesMapper.getMedicalRecordStats();

        // 如果结果为null，则返回空对象
        if (stats == null) {
            return new MedicalRecordStatsVO(0, 0, 0);
        }

        return stats;
    }

    @Override
    public CheckProjectStatsVO getCheckProjectStats() {
        // 使用Mapper直接查询统计数据
        CheckProjectStatsVO stats = checkProjectsMapper.getCheckProjectStats();

        // 如果结果为null，则返回空对象
        if (stats == null) {
            return new CheckProjectStatsVO(0, "无");
        }

        return stats;
    }

    @Override
    public AppointmentWeekDistributionVO getAppointmentWeeklyDistribution() {
        // 获取本周一和周日的日期
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        LocalDate sunday = today.with(DayOfWeek.SUNDAY);

        // 格式化日期为字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDate = monday.format(formatter);
        String endDate = sunday.format(formatter);

        // 获取每周预约分布数据
        List<Map<String, Object>> distributionData = appointmentsMapper.getWeeklyDistribution(startDate, endDate);

        // 准备数据容器
        List<Integer> data = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0)); // 默认值为0
        List<String> labels = Arrays.asList("周一", "周二", "周三", "周四", "周五", "周六", "周日");

        // 填充统计数据
        for (Map<String, Object> item : distributionData) {
            int dayOfWeek = ((Number) item.get("day_of_week")).intValue();
            int count = ((Number) item.get("appointment_count")).intValue();

            // MySQL的DAYOFWEEK从周日(1)到周六(7)，需要转换为周一(0)到周日(6)的索引
            int index = dayOfWeek == 1 ? 6 : dayOfWeek - 2;
            data.set(index, count);
        }

        return new AppointmentWeekDistributionVO(data, labels);
    }

    @Override
    public PatientGrowthTrendVO getPatientGrowthTrend() {
        // 获取月度增长数据
        List<Map<String, Object>> growthData = patientsMapper.getMonthlyGrowthTrend();

        // 准备数据容器
        List<Integer> data = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        // 填充过去12个月的数据，不足的月份补0
        Map<String, Integer> monthlyData = new HashMap<>();

        // 初始化过去12个月的数据
        LocalDate now = LocalDate.now();
        for (int i = 11; i >= 0; i--) {
            LocalDate date = now.minusMonths(i);
            String monthKey = date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            monthlyData.put(monthKey, 0);
        }

        // 填充实际数据
        for (Map<String, Object> item : growthData) {
            String month = (String) item.get("month");
            int count = ((Number) item.get("patient_count")).intValue();
            if (monthlyData.containsKey(month)) {
                monthlyData.put(month, count);
            }
        }

        // 按月份顺序整理数据
        for (int i = 11; i >= 0; i--) {
            LocalDate date = now.minusMonths(i);
            String monthKey = date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            String monthLabel = date.format(DateTimeFormatter.ofPattern("M月"));

            labels.add(monthLabel);
            data.add(monthlyData.getOrDefault(monthKey, 0));
        }

        return new PatientGrowthTrendVO(data, labels);
    }
}
