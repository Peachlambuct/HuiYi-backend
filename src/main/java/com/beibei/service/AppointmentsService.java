package com.beibei.service;

import com.beibei.entity.dto.Appointments;
import com.baomidou.mybatisplus.extension.service.IService;
import com.beibei.entity.vo.request.AppointQuery;
import com.beibei.entity.vo.request.ChoosePropVO;
import com.beibei.entity.vo.request.CreateAppointmentVO;
import com.beibei.entity.vo.response.AppointmentCardVO;
import com.beibei.entity.vo.response.AppointmentChoose;
import com.beibei.entity.vo.response.LastAppointmentVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@Service
public interface AppointmentsService extends IService<Appointments> {
    void increase(CreateAppointmentVO vo, Long userId);

    List<AppointmentCardVO> getAppointmentCardVOListByUserId(Long userId);

    List<AppointmentCardVO> queryAppointments(Long userId, AppointQuery query);

    LastAppointmentVO getLatestAppointmentByPatientID(Long userId);

    List<AppointmentChoose> getAppointmentChoose(ChoosePropVO vo);

    List<AppointmentCardVO> getNonFinishedList(Long userId);

    Long CountAppointmentsIncludingDeleted(Long userId);
}
