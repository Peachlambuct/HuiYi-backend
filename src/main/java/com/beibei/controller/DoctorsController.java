package com.beibei.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beibei.entity.RestBean;
import com.beibei.entity.dto.Checks;
import com.beibei.entity.dto.Doctors;
import com.beibei.entity.dto.Users;
import com.beibei.entity.vo.request.DoctorQuery;
import com.beibei.entity.vo.response.DoctorTodayAppoint;
import com.beibei.entity.vo.response.NonFinishCase;
import com.beibei.service.ChecksService;
import com.beibei.service.DoctorsService;
import com.beibei.service.UsersService;
import com.beibei.utils.Const;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@RestController
@RequestMapping("/api/doctor")
public class DoctorsController {
    @Resource
    private DoctorsService service;
    @Resource
    private ChecksService checksService;
    @Resource
    private UsersService usersService;

    @PostMapping("/create")
    public RestBean<Void> createDoctor(Doctors doctors) {
        service.save(doctors);
        return RestBean.success();
    }

    @PostMapping("/update")
    public RestBean<Void> updateDoctor(Doctors doctors) {
        service.updateById(doctors);
        return RestBean.success();
    }

    @PostMapping("/add")
    public RestBean<Void> addCheck(Checks checks) {
        checksService.save(checks);
        return RestBean.success();
    }

    @GetMapping("/jobtype")
    public RestBean<List<String>> getJobType() {
        return RestBean.success(service.getJobTypes());
    }

    @PostMapping("/query")
    public RestBean<List<Doctors>> query(@RequestBody DoctorQuery query) {
        return RestBean.success(service.queryDoctor(query));
    }

    @GetMapping("/todayappoint")
    public RestBean<List<DoctorTodayAppoint>> getAppointmentsByDoctorUserID(
            @RequestAttribute("userId") Integer userId) {
        try {
            Long doctorID = service.getOne(new QueryWrapper<Doctors>().eq("user_id", userId)).getId();
            if (doctorID == null) {
                return RestBean.error(new Exception("该用户ID不存在"));
            }

            List<DoctorTodayAppoint> appointments = service.getAppointmentsByDoctorId(doctorID);
            LocalDate currentDate = LocalDate.now();

            List<DoctorTodayAppoint> todayAppointments = appointments.stream()
                    .peek(appointment -> {
                        appointment.setAge(calculateAge(appointment.getBirthday().toLocalDate()));
                        appointment.setDate(constructDate(appointment.getYear(), appointment.getMonth(),
                                appointment.getDay(), appointment.getTimeId()));
                    })
                    .filter(appointment -> appointment.getYear() == currentDate.getYear() &&
                            appointment.getMonth() == currentDate.getMonthValue() &&
                            appointment.getDay() == currentDate.getDayOfMonth())
                    .collect(Collectors.toList());
            return RestBean.success(todayAppointments);
        } catch (Exception e) {
            return RestBean.error(e);
        }
    }

    @GetMapping("/nonfinishcases")
    public RestBean<List<NonFinishCase>> getNonFinishCases(@RequestAttribute("userId") Integer userId) {
        try {
            Long doctorID = service.getOne(new QueryWrapper<Doctors>().eq("user_id", userId)).getId();
            if (doctorID == null) {
                return RestBean.error(new Exception("该用户ID不存在"));
            }

            List<NonFinishCase> nonFinishCases = service.getCasesByDoctorIdAndStatus(doctorID);
            nonFinishCases = nonFinishCases.stream().peek(it -> {
                it.setAge(calculateAge(it.getBirthday().toLocalDate()));
            }).collect(Collectors.toList());
            return RestBean.success(nonFinishCases);

        } catch (Exception e) {
            return RestBean.error(e);

        }
    }

    @GetMapping("/info")
    public RestBean<Doctors> getDoctorInfo(@RequestAttribute(Const.ATTR_USER_ID) Long userId) {
        Doctors doctor = service.getDoctorByUserId(userId);
        return RestBean.success(doctor);
    }

    @GetMapping("/getDoctorInfo")
    public RestBean<Doctors> getDoctorInfoById(@RequestParam Long doctorId) {
        Doctors doctor = service.getById(doctorId);
        Users byId = usersService.getById(doctor.getUserId());
        doctor.setAvatar(byId.getAvatar());
        return RestBean.success(doctor);
    }

    private int calculateAge(LocalDate birthday) {
        return LocalDate.now().getYear() - birthday.getYear();
    }

    private String constructDate(int year, int month, int day, int timeId) {
        Map<Integer, String> timeMap = new HashMap<>();
        timeMap.put(1, "08:00-09:00");
        timeMap.put(2, "09:00-10:00");
        timeMap.put(3, "10:00-11:00");
        timeMap.put(4, "11:00-12:00");
        timeMap.put(5, "14:00-15:00");
        timeMap.put(6, "15:00-16:00");
        timeMap.put(7, "16:00-17:00");
        // Add more mappings as needed

        String timeRange = timeMap.getOrDefault(timeId, "Unknown");

        return String.format("%04d-%02d-%02d %s", year, month, day, timeRange);
    }
}
