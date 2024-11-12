package com.beibei.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beibei.entity.dto.Appointments;
import com.beibei.entity.vo.response.DoctorTodayAppoint;
import com.beibei.mapper.AppointmentsMapper;
import com.beibei.service.AppointmentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@Slf4j
@Service
public class AppointmentsServiceImpl extends ServiceImpl<AppointmentsMapper, Appointments> implements AppointmentsService {

}
