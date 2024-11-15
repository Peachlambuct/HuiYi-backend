package com.beibei.controller;

import com.beibei.entity.RestBean;
import com.beibei.entity.dto.Appointments;
import com.beibei.entity.vo.request.AppointQuery;
import com.beibei.entity.vo.request.ChoosePropVO;
import com.beibei.entity.vo.request.CreateAppointmentVO;
import com.beibei.entity.vo.request.UpdateAppointmentVO;
import com.beibei.entity.vo.response.AppointmentCardVO;
import com.beibei.entity.vo.response.AppointmentChoose;
import com.beibei.entity.vo.response.LastAppointmentVO;
import com.beibei.service.AppointmentsService;
import com.beibei.utils.Const;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@RestController
@RequestMapping("/api/appointment")
public class AppointmentsController {
    @Resource
    private AppointmentsService appointmentsService;

    @PostMapping("/increase")
    public RestBean<Void> increase(@RequestBody CreateAppointmentVO vo, @RequestAttribute(Const.ATTR_USER_ID) Long userId) {
        appointmentsService.increase(vo, userId);
        return RestBean.success();
    }

    @GetMapping("/list")
    public RestBean<List<AppointmentCardVO>> list(@RequestAttribute(Const.ATTR_USER_ID) Long userId) {
        return RestBean.success(appointmentsService.getAppointmentCardVOListByUserId(userId));
    }

    @GetMapping("/delete")
    public RestBean<Void> delete(@RequestParam("id") Long id) {
        Appointments appointments = appointmentsService.getById(id);
        if (appointments == null) {
            return RestBean.error(new Exception("未找到该预约"));
        }
        appointments.setDeletedAt(LocalDateTime.now());
        appointmentsService.saveOrUpdate(appointments);
        return RestBean.success();
    }

    @PostMapping("/update")
    public RestBean<Void> update(@RequestBody UpdateAppointmentVO vo) {
        Appointments appointmentsServiceById = appointmentsService.getById(vo.getId());
        if (appointmentsServiceById == null) {
            return RestBean.error(new Exception("未找到该预约"));
        }
        appointmentsServiceById.setTimeId(vo.getTimeId());
        appointmentsService.saveOrUpdate(appointmentsServiceById);
        return RestBean.success();
    }

    @PostMapping("/query")
    public RestBean<List<AppointmentCardVO>> query(@RequestBody AppointQuery query, @RequestAttribute(Const.ATTR_USER_ID) Long userId) {
        return RestBean.success(appointmentsService.queryAppointments(userId, query));
    }

    @GetMapping("/latest")
    public RestBean<LastAppointmentVO> latest(@RequestAttribute(Const.ATTR_USER_ID) Long userId) {
        return RestBean.success(appointmentsService.getLatestAppointmentByPatientID(userId));
    }

    @PostMapping("/choose")
    public RestBean<List<AppointmentChoose>> choose(@RequestBody @Valid ChoosePropVO vo) {
        return RestBean.success(appointmentsService.getAppointmentChoose(vo));
    }

    @GetMapping("/nonfinished")
    public RestBean<List<AppointmentCardVO>> nonfinished(@RequestAttribute(Const.ATTR_USER_ID) Long userId) {
        return RestBean.success(appointmentsService.getNonFinishedList(userId));
    }

    @GetMapping("/sum")
    public RestBean<Long> sum(@RequestAttribute(Const.ATTR_USER_ID) Long userId) {
        return RestBean.success(appointmentsService.CountAppointmentsIncludingDeleted(userId));
    }

}
