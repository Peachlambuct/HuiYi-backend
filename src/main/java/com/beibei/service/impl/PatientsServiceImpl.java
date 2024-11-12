package com.beibei.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.beibei.entity.dto.Patients;
import com.beibei.entity.dto.Users;
import com.beibei.entity.vo.request.CreatePatientVO;
import com.beibei.entity.vo.response.CheckInfoVO;
import com.beibei.entity.vo.response.PatientInfoVO;
import com.beibei.mapper.ChecksMapper;
import com.beibei.mapper.PatientsMapper;
import com.beibei.service.PatientsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beibei.service.UsersService;
import jakarta.annotation.Resource;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@Service
public class PatientsServiceImpl extends ServiceImpl<PatientsMapper, Patients> implements PatientsService {
    @Resource
    private UsersService usersService;
    @Resource
    private ChecksMapper checksMapper;

    @Override
    public void createPatient(CreatePatientVO vo, Long userId) {
        Patients patients = new Patients();
        BeanUtil.copyProperties(vo, patients);
        patients.setUserId(userId);
        Users user = usersService.getById(userId);
        user.setStatus(true);
        user.setRole("patient");
        usersService.updateById(user);
        this.save(patients);
    }

    @Override
    public PatientInfoVO getPatientInfo(Long userId) {
        Users user = usersService.getById(userId);
        Patients patients = this.lambdaQuery().eq(Patients::getUserId, userId).one();
        PatientInfoVO patientInfoVO = new PatientInfoVO();
        BeanUtil.copyProperties(patients, patientInfoVO);
        BeanUtil.copyProperties(user, patientInfoVO);
        if (patients.getSex()) {
            patientInfoVO.setSex("男");
        } else {
            patientInfoVO.setSex("女");
        }
        //根据出生日期计算年龄
        patientInfoVO.setAge(LocalDateTime.now().getYear() - patients.getBirthday().getYear());
        return patientInfoVO;
    }

    @Override
    public void updatePatient(CreatePatientVO vo, Long userId) {
        Patients patients = this.lambdaQuery().eq(Patients::getUserId, userId).one();
        BeanUtil.copyProperties(vo, patients);
        this.saveOrUpdate(patients);
    }

    @Override
    public List<CheckInfoVO> getCheckInfo(Long userId) {
        Patients patient = this.lambdaQuery().eq(Patients::getUserId, userId).one();
        if (patient == null) {
            return List.of();
        }
        return checksMapper.getCheckInfoByPatientId(patient.getId());
    }
}
