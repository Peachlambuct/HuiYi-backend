package com.beibei.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beibei.entity.dto.*;
import com.beibei.entity.vo.request.CaseQuery;
import com.beibei.entity.vo.request.CheckInfo;
import com.beibei.entity.vo.request.ResponseCaseInfoVO;
import com.beibei.mapper.CasesMapper;
import com.beibei.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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

    @Override
    public void updateCase(ResponseCaseInfoVO vo) {
        Cases cases = new Cases();
        BeanUtil.copyProperties(vo, cases);
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
    public ResponseCaseInfoVO getCaseInfo(Long id) {
        Cases cases = this.getById(id);
        Doctors doctors = doctorsService.getById(cases.getDoctorId());
        Patients patients = patientsService.getById(cases.getPatientId());
        ResponseCaseInfoVO responseCaseInfoVO = new ResponseCaseInfoVO();
        BeanUtil.copyProperties(cases, responseCaseInfoVO);
        if (StrUtil.isBlank(cases.getCheckId())) {
            responseCaseInfoVO.setDoctorName(doctors.getName());
            responseCaseInfoVO.setDoctorId(doctors.getId());
            responseCaseInfoVO.setDoctorType(doctors.getJobType());

            responseCaseInfoVO.setPatientName(patients.getName());
            responseCaseInfoVO.setAge(calculateAge(patients.getBirthday().toLocalDate()));
            responseCaseInfoVO.setSex(patients.getSex());
            responseCaseInfoVO.setPatientId(patients.getId());
            return responseCaseInfoVO;
        }
        List<String> checkId = Arrays.stream(cases.getCheckId().split(",")).toList();
        List<Checks> checks = checksService.list(new QueryWrapper<Checks>().in("id", checkId));
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
}
