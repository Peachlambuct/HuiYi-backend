package com.beibei.service.impl;

import com.beibei.entity.dto.Patients;
import com.beibei.mapper.PatientsMapper;
import com.beibei.service.IPatientsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@Service
public class PatientsServiceImpl extends ServiceImpl<PatientsMapper, Patients> implements IPatientsService {

}
