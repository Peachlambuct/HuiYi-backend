package com.beibei.service;

import com.beibei.entity.dto.Doctors;
import com.baomidou.mybatisplus.extension.service.IService;
import com.beibei.entity.vo.request.DoctorQuery;
import com.beibei.entity.vo.response.DoctorTodayAppoint;
import com.beibei.entity.vo.response.NonFinishCase;
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
public interface DoctorsService extends IService<Doctors> {
    List<String> getJobTypes();
    List<Doctors> queryDoctor(DoctorQuery query);
    List<DoctorTodayAppoint> getAppointmentsByDoctorId(Long doctorID);
    List<NonFinishCase> getCasesByDoctorIdAndStatus(Long doctorId);
}
