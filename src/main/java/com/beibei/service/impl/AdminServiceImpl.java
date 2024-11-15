package com.beibei.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.beibei.entity.dto.CheckProjects;
import com.beibei.entity.dto.Doctors;
import com.beibei.entity.dto.Patients;
import com.beibei.entity.dto.Users;
import com.beibei.entity.vo.request.AdminLoginVO;
import com.beibei.entity.vo.request.UpdateCheckProjectVO;
import com.beibei.entity.vo.response.CountResponseVO;
import com.beibei.entity.vo.response.DoctorCard;
import com.beibei.entity.vo.response.PatientInfoVO;
import com.beibei.service.*;
import com.beibei.utils.GenerateRandomKeyUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        return checkProjectService.list();
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
}
