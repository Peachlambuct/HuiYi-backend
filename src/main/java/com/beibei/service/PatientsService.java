package com.beibei.service;

import com.beibei.entity.dto.Patients;
import com.baomidou.mybatisplus.extension.service.IService;
import com.beibei.entity.vo.request.CreatePatientVO;
import com.beibei.entity.vo.response.CheckInfoVO;
import com.beibei.entity.vo.response.PatientInfoVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@Service
public interface PatientsService extends IService<Patients> {
    Patients getPatientByUserId(Long userId);
    void createPatient(CreatePatientVO vo, Long userId);
    PatientInfoVO getPatientInfo(Long userId);
    void updatePatient(CreatePatientVO vo, Long userId);
    List<CheckInfoVO> getCheckInfo(Long userId);
}
