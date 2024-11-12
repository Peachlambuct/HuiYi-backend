package com.beibei.controller;

import com.beibei.entity.RestBean;
import com.beibei.entity.vo.request.CreatePatientVO;
import com.beibei.entity.vo.response.CheckInfoVO;
import com.beibei.entity.vo.response.PatientInfoVO;
import com.beibei.service.ChecksService;
import com.beibei.service.PatientsService;
import com.beibei.utils.Const;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@RestController
@RequestMapping("/api/patient")
public class PatientsController {
    @Resource
    private PatientsService patientsService;
    @Resource
    private ChecksService checksService;

    @PostMapping("/create")
    public RestBean<Void> createPatient(@RequestBody CreatePatientVO createPatientVO, @RequestAttribute(Const.ATTR_USER_ID) Long userId) {
        patientsService.createPatient(createPatientVO, userId);
        return RestBean.success();
    }

    @GetMapping("/info")
    public RestBean<PatientInfoVO> getPatientInfo(@RequestAttribute(Const.ATTR_USER_ID) Long userId) {
        return RestBean.success(patientsService.getPatientInfo(userId));
    }

    @PostMapping("/update")
    public RestBean<Void> updatePatient(@RequestBody CreatePatientVO createPatientVO, @RequestAttribute(Const.ATTR_USER_ID) Long userId) {
        patientsService.updatePatient(createPatientVO, userId);
        return RestBean.success();
    }

    @GetMapping("/infobyid")
    public RestBean<PatientInfoVO> getPatientInfoById(@RequestParam("pid") Long pid) {
        return RestBean.success(patientsService.getPatientInfo(pid));
    }

    @GetMapping("/checkinfo")
    public RestBean<List<CheckInfoVO>> getCheckInfo(@RequestAttribute(Const.ATTR_USER_ID) Long userId) {
        return RestBean.success(patientsService.getCheckInfo(userId));
    }

    @GetMapping("/finsh")
    public RestBean<Void> finishCheck(@RequestParam("id") Long id) {
        checksService.finishCheck(id);
        return RestBean.success();
    }
}
