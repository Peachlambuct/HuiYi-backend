package com.beibei.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beibei.entity.dto.Doctors;
import com.beibei.entity.vo.request.DoctorQuery;
import com.beibei.entity.vo.response.DoctorCard;
import com.beibei.entity.vo.response.DoctorTodayAppoint;
import com.beibei.entity.vo.response.NonFinishCase;
import com.beibei.mapper.DoctorsMapper;
import com.beibei.service.DoctorsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
public class DoctorsServiceImpl extends ServiceImpl<DoctorsMapper, Doctors> implements DoctorsService {

    @Override
    public List<String> getJobTypes() {
        return baseMapper.getJobTypes();
    }

    @Override
    public List<Doctors> queryDoctor(DoctorQuery query) {
        QueryWrapper<Doctors> wrapper = new QueryWrapper<>();
        if (!StrUtil.isBlank(query.getDoctorType())) {
            wrapper.eq("job_type", query.getDoctorType());
        }
        if (!StrUtil.isBlank(query.getDoctorName())) {
            wrapper.like("name", query.getDoctorName());
        }

        return this.list(wrapper);
    }

    @Override
    public List<DoctorTodayAppoint> getAppointmentsByDoctorId(Long doctorID) {
        return baseMapper.getAppointmentsByDoctorId(doctorID);
    }

    @Override
    public List<NonFinishCase> getCasesByDoctorIdAndStatus(Long doctorId) {
        return baseMapper.getCasesByDoctorIdAndStatus(doctorId);
    }

    @Override
    public List<DoctorCard> findAllDoctors() {
        return baseMapper.findAllDoctors();
    }
}
