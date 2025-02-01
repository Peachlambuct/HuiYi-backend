package com.beibei.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beibei.entity.RestBean;
import com.beibei.entity.dto.Cases;
import com.beibei.entity.dto.Patients;
import com.beibei.entity.vo.request.CaseQuery;
import com.beibei.entity.vo.request.ResponseCaseInfoVO;
import com.beibei.service.CasesService;
import com.beibei.service.PatientsService;
import com.beibei.utils.Const;
import jakarta.annotation.Resource;
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
@RequestMapping("/api/cases")
public class CasesController {
    @Resource
    private CasesService casesService;
    @Resource
    private PatientsService patientsService;

    @PostMapping("/increase")
    public RestBean<Void> increase(@RequestBody Cases cases) {
        casesService.save(cases);
        return RestBean.success();
    }

    @GetMapping("/list")
    public RestBean<List<Cases>> list() {
        casesService.list(new QueryWrapper<Cases>().isNotNull("deleted_at"));
        return RestBean.success();
    }

    @GetMapping("/delete")
    public RestBean<Void> delete(@RequestParam Integer id) {
        Cases cases = casesService.getById(id);
        cases.setDeletedAt(LocalDateTime.now());
        casesService.updateById(cases);
        return RestBean.success();
    }

    @PostMapping("/update")
    public RestBean<Void> update(@RequestBody ResponseCaseInfoVO vo) {
        casesService.updateCase(vo);
        return RestBean.success();
    }

    @GetMapping("/info")
    public RestBean<Void> info(@RequestParam("id") Integer id) {
        casesService.getById(id);
        return RestBean.success();
    }

    @GetMapping("/latest")
    public RestBean<Cases> latest(@RequestAttribute(Const.ATTR_USER_ID) Long userId) {
        Long patientId = patientsService.getOne(new QueryWrapper<Patients>().eq("user_id", userId)).getId();
        List<Cases> list = casesService
                .list(new QueryWrapper<Cases>().eq("patient_id", patientId).orderByDesc("created_at"));
        return RestBean.success(list.getFirst());
    }

    @PostMapping("/query")
    public RestBean<List<Cases>> query(@RequestBody CaseQuery query,
            @RequestAttribute(Const.ATTR_USER_ID) Long userId) {
        return RestBean.success(casesService.queryCase(query, userId));
    }

    @GetMapping("/details")
    public RestBean<ResponseCaseInfoVO> details(@RequestParam("case_id") Long caseId) {
        return RestBean.success(casesService.getCaseInfo(caseId));
    }
}
