package com.beibei.service;

import com.beibei.entity.dto.CheckProjects;
import com.beibei.entity.vo.request.AdminLoginVO;
import com.beibei.entity.vo.request.UpdateCheckProjectVO;
import com.beibei.entity.vo.response.CountResponseVO;
import com.beibei.entity.vo.response.DoctorCard;
import com.beibei.entity.vo.response.PatientInfoVO;
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
}
