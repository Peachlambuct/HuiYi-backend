package com.beibei.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beibei.entity.dto.Appointments;
import com.beibei.entity.dto.Cases;
import com.beibei.entity.dto.Patients;
import com.beibei.entity.vo.request.AppointQuery;
import com.beibei.entity.vo.request.ChoosePropVO;
import com.beibei.entity.vo.request.CreateAppointmentVO;
import com.beibei.entity.vo.response.AppointmentCardVO;
import com.beibei.entity.vo.response.AppointmentChoose;
import com.beibei.entity.vo.response.DoctorTodayAppoint;
import com.beibei.entity.vo.response.LastAppointmentVO;
import com.beibei.mapper.AppointmentsMapper;
import com.beibei.service.AppointmentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beibei.service.CasesService;
import com.beibei.service.PatientsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@Slf4j
@Service
public class AppointmentsServiceImpl extends ServiceImpl<AppointmentsMapper, Appointments> implements AppointmentsService {
    @Resource
    private PatientsService patientsService;
    @Resource
    private CasesService casesService;

    @Override
    public void increase(CreateAppointmentVO vo, Long userId) {
        String[] split = vo.getTime().split("-");
        String year = split[0];
        String month = split[1];
        String day = split[2];
        Patients patientId = patientsService.getPatientByUserId(userId);
        Appointments appointments = new Appointments();
        BeanUtil.copyProperties(vo, appointments);
        appointments.setPatientId(patientId.getId());
        appointments.setYear(year);
        appointments.setMonth(month);
        appointments.setDay(day);
        this.save(appointments);

        // 创建一个新的病例记录，初始状态为0
        Cases cases = new Cases();
        cases.setStatus(false);
        cases.setDoctorId(vo.getDoctorId());
        cases.setPatientId(patientId.getId());
        casesService.save(cases);
        log.info("用户{}预约了医生{}，时间为{}-{}-{}", userId, vo.getDoctorId(), year, month, day);
    }

    @Override
    public List<AppointmentCardVO> getAppointmentCardVOListByUserId(Long userId) {
        List<AppointmentCardVO> appointmentCards = baseMapper.getAppointmentCardsByUserId(userId);

        for (AppointmentCardVO card : appointmentCards) {
            card.setDate(constructDate(card.getYear(), card.getMonth(), card.getDay(), card.getTimeId()));
        }

        return appointmentCards;
    }

    @Override
    public List<AppointmentCardVO> queryAppointments(Long userId, AppointQuery query) {
        Long patientId = patientsService.getPatientByUserId(userId).getId();
        if (patientId == null) {
            throw new RuntimeException("Patient ID not found for user ID: " + userId);
        }

        List<AppointmentCardVO> appointmentCards = baseMapper.queryAppointments(query.getFrom(), query.getTo(), patientId, query.getStatus());

        for (AppointmentCardVO card : appointmentCards) {
            card.setDate(constructDate(card.getYear(), card.getMonth(), card.getDay(), card.getTimeId()));
        }

        return appointmentCards;
    }

    @Override
    public LastAppointmentVO getLatestAppointmentByPatientID(Long userId) {
        Long patientId = patientsService.getPatientByUserId(userId).getId();
        if (patientId == null) {
            throw new RuntimeException("Patient ID not found for user ID: " + userId);
        }

        LastAppointmentVO latestAppointment = baseMapper.getLatestAppointmentByPatientId(patientId);
        if (latestAppointment == null) {
            return null;
        }

        latestAppointment.setDate(constructDate(
                latestAppointment.getYear(),
                latestAppointment.getMonth(),
                latestAppointment.getDay(),
                latestAppointment.getTimeId()));

        return latestAppointment;
    }

    @Override
    public List<AppointmentChoose> getAppointmentChoose(ChoosePropVO vo) {
        String[] split = vo.getDate().split("-");
        if (split.length != 3) {
            throw new RuntimeException("Invalid date format: " + vo.getDate());
        }
        String year = split[0];
        String month = split[1];
        String day = split[2];

        List<Appointments> appointments = this.list(new QueryWrapper<Appointments>().eq("year", year).eq("month", month).eq("day", day));
        HashSet<Long> timeIds = new HashSet<>();

        appointments.forEach(it -> timeIds.add(it.getTimeId()));
        ArrayList<AppointmentChoose> appointmentChooses = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            AppointmentChoose choose = new AppointmentChoose();
            if (!timeIds.contains((long) i)) {
                choose.setStatus(0);
            } else {
                choose.setStatus(1);
            }
            choose.setVal(i);
            appointmentChooses.add(choose);
        }
        return appointmentChooses;
    }

    @Override
    public List<AppointmentCardVO> getNonFinishedList(Long userId) {
        Long patientId = patientsService.getPatientByUserId(userId).getId();
        if (patientId == null) {
            throw new RuntimeException("Patient ID not found for user ID: " + userId);
        }
        List<AppointmentCardVO> appointmentCards = baseMapper.getAppointmentCardsByUserId(patientId);
        appointmentCards.stream()
                .filter(it -> !it.isStatus())
                .forEach(it -> it.setDate(constructDate(it.getYear(), it.getMonth(), it.getDay(), it.getTimeId())));
        return appointmentCards;
    }

    @Override
    public Long CountAppointmentsIncludingDeleted(Long userId) {
        Long patientId = patientsService.getPatientByUserId(userId).getId();
        if (patientId == null) {
            throw new RuntimeException("Patient ID not found for user ID: " + userId);
        }
        List<Appointments> appointments = this.list(new QueryWrapper<Appointments>().eq("patient_id", patientId));
        if (appointments != null) {
            return (long) appointments.size();
        }
        return 0L;
    }

    private String constructDate(int year, int month, int day, int timeId) {
        String[] timeMap = {
                "Unknown", "08:00-09:00", "09:00-10:00", "10:00-11:00", "11:00-12:00",
                "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00"
        };

        String timeRange = (timeId >= 1 && timeId < timeMap.length) ? timeMap[timeId] : "Unknown";

        return String.format("%04d-%02d-%02d %s", year, month, day, timeRange);
    }
}
