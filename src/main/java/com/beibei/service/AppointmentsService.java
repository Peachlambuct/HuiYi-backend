package com.beibei.service;

import com.beibei.entity.dto.Appointments;
import com.baomidou.mybatisplus.extension.service.IService;
import com.beibei.entity.vo.response.DoctorTodayAppoint;
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
public interface AppointmentsService extends IService<Appointments> {
}
