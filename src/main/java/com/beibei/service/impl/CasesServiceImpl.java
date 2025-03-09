package com.beibei.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.beibei.entity.dto.*;
import com.beibei.service.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beibei.entity.vo.request.CaseQuery;
import com.beibei.entity.vo.request.CheckInfo;
import com.beibei.entity.vo.request.ResponseCaseInfoVO;
import com.beibei.entity.vo.response.CaseInfoVO;
import com.beibei.entity.vo.response.CheckItemVO;
import com.beibei.mapper.CasesMapper;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@Service
public class CasesServiceImpl extends ServiceImpl<CasesMapper, Cases> implements CasesService {
    @Resource
    private PatientsService patientsService;
    @Resource
    private DoctorsService doctorsService;
    @Resource
    private ChecksService checksService;
    @Resource
    private CheckProjectsService checkProjectsService;
    @Lazy
    @Resource
    private AppointmentsService appointmentsService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCase(ResponseCaseInfoVO vo) {
        Long aid = this.getById(vo.getId()).getAid();
        Cases cases = new Cases();
        cases.setAid(aid);
        BeanUtil.copyProperties(vo, cases);
        if (StrUtil.isNotBlank(vo.getContent())&& cases.getAid() != null) {
            Appointments appointments = appointmentsService.getById(cases.getAid());
            appointments.setStatus(true);
            appointmentsService.updateById(appointments);
            cases.setStatus(true);
        }
        this.updateById(cases);
    }

    @Override
    public List<Cases> queryCase(CaseQuery query, Long userId) {
        Long patientId = patientsService.getOne(new QueryWrapper<Patients>().eq("user_id", userId)).getId();
        QueryWrapper<Cases> wrapper = new QueryWrapper<Cases>().eq("patient_id", patientId);
        if (query.getTo() != null) {
            wrapper.le("created_at", query.getTo());
        }
        if (query.getFrom() != null) {
            wrapper.ge("created_at", query.getFrom());
        }
        if (!StrUtil.isBlank(query.getTitle())) {
            wrapper.like("title", query.getTitle());
        }
        return this.list(wrapper);
    }

    @Override
    public ResponseCaseInfoVO getResponseCaseInfo(Long id) {
        Cases cases = this.getById(id);
        Doctors doctors = doctorsService.getById(cases.getDoctorId());
        Patients patients = patientsService.getById(cases.getPatientId());
        ResponseCaseInfoVO responseCaseInfoVO = new ResponseCaseInfoVO();
        BeanUtil.copyProperties(cases, responseCaseInfoVO);

        List<Checks> checks = checksService.list(new QueryWrapper<Checks>().eq("case_id", cases.getId()));
        if (CollUtil.isEmpty(checks)) {
            responseCaseInfoVO.setDoctorName(doctors.getName());
            responseCaseInfoVO.setDoctorId(doctors.getId());
            responseCaseInfoVO.setDoctorType(doctors.getJobType());

            responseCaseInfoVO.setPatientName(patients.getName());
            responseCaseInfoVO.setAge(calculateAge(patients.getBirthday().toLocalDate()));
            responseCaseInfoVO.setSex(patients.getSex());
            responseCaseInfoVO.setPatientId(patients.getId());
            return responseCaseInfoVO;
        }
        HashSet<Long> checksProjectId = new HashSet<>();
        for (Checks check : checks) {
            checksProjectId.add(check.getCheckProjectId());
        }
        List<CheckProjects> checkProjects = checkProjectsService
                .list(new QueryWrapper<CheckProjects>().in("id", checksProjectId));
        List<CheckInfo> checkInfos = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Checks check : checks) {
            for (CheckProjects project : checkProjects) {
                if (check.getCheckProjectId().equals(project.getId())) {
                    CheckInfo checkInfo = new CheckInfo();
                    checkInfo.setName(project.getName());
                    checkInfo.setRoom(project.getRoom());
                    checkInfo.setImg(project.getImg());
                    checkInfo.setStatus(check.getStatus());
                    checkInfo.setTime(check.getCreatedAt().format(formatter));
                    checkInfos.add(checkInfo);
                }
            }
        }

        ResponseCaseInfoVO caseInfo = new ResponseCaseInfoVO();
        caseInfo.setId(cases.getId());
        caseInfo.setTitle(cases.getTitle());
        caseInfo.setContent(cases.getContent());
        caseInfo.setDoctorName(doctors.getName());
        caseInfo.setDoctorId(doctors.getId());
        caseInfo.setCheckProject(checkInfos);
        caseInfo.setCheckId(cases.getCheckId());
        caseInfo.setPatientName(patients.getName());
        caseInfo.setPatientId(patients.getId());
        caseInfo.setSex(patients.getSex());
        caseInfo.setAge(calculateAge(patients.getBirthday().toLocalDate()));
        caseInfo.setDoctorType(doctors.getJobType());
        caseInfo.setDate(cases.getCreatedAt().format(formatter));

        return caseInfo;
    }

    private int calculateAge(LocalDate birthday) {
        return LocalDate.now().getYear() - birthday.getYear();
    }

    @Override
    public CaseInfoVO getCaseInfo(Long caseId) {
        // 获取病例信息
        Cases cases = this.getById(caseId);
        if (cases == null) {
            throw new RuntimeException("病例不存在");
        }

        // 获取医生信息
        Doctors doctor = doctorsService.getById(cases.getDoctorId());
        if (doctor == null) {
            throw new RuntimeException("医生信息不存在");
        }

        // 获取患者信息
        Patients patient = patientsService.getById(cases.getPatientId());
        if (patient == null) {
            throw new RuntimeException("患者信息不存在");
        }

        // 计算患者年龄
        int age = calculateAge(patient.getBirthday().toLocalDate());

        // 获取检查项目
        List<CheckItemVO> checkItems = new ArrayList<>();
        if (cases.getCheckId() != null && !cases.getCheckId().isEmpty()) {
            String[] checkIds = cases.getCheckId().split(",");
            for (String checkId : checkIds) {
                if (checkId.isEmpty())
                    continue;
                try {
                    Long id = Long.parseLong(checkId);
                    Checks check = checksService.getById(id);
                    if (check != null) {
                        // 获取检查项目信息
                        CheckProjects project = checkProjectsService.getById(check.getCheckProjectId());
                        if (project != null) {
                            CheckItemVO item = new CheckItemVO();
                            item.setName(project.getName());
                            item.setRoom(project.getRoom());
                            item.setImg(project.getImg() != null ? project.getImg() : "");
                            item.setStatus(check.getStatus());
                            item.setTime(check.getCreatedAt() != null
                                    ? check.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                    : "");
                            checkItems.add(item);
                        }
                    }
                } catch (NumberFormatException e) {
                    // 忽略无效的检查ID
                }
            }
        }

        // 构建返回结果
        CaseInfoVO result = new CaseInfoVO();
        result.setId(caseId); // 使用Long类型
        result.setTitle(cases.getTitle());
        result.setDoctorName(doctor.getName());
        result.setDoctorType(doctor.getJobType());
        result.setDoctorId(doctor.getId()); // 使用Long类型
        result.setDoctorIdStr(doctor.getId().toString()); // 设置String类型的ID
        result.setCheckProject(checkItems);
        result.setContent(cases.getContent());
        result.setSex(patient.getSex());
        result.setPatientName(patient.getName());
        result.setPatientId(patient.getId()); // 使用Long类型
        result.setPatientIdStr(patient.getId().toString()); // 设置String类型的ID
        result.setAge(age);
        result.setDate(cases.getCreatedAt() != null
                ? cases.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : "");
        result.setCheckId(cases.getCheckId());

        return result;
    }
}
