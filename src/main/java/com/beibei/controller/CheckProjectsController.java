package com.beibei.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beibei.entity.RestBean;
import com.beibei.entity.dto.CheckProjects;
import com.beibei.service.CheckProjectsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-09
 */
@RestController
@RequestMapping("/api/checkProject")
public class CheckProjectsController {
    @Resource
    private CheckProjectsService checkProjectsService;

    @GetMapping("/list")
    public RestBean<List<CheckProjects>> list() {
        List<CheckProjects> list = checkProjectsService.list(new QueryWrapper<CheckProjects>().isNull("deleted_at"));
        return RestBean.success(list);
    }

    @PostMapping("/create")
    public RestBean<Void> create(@RequestBody CheckProjects checkProjects) {
        checkProjectsService.save(checkProjects);
        return RestBean.success();
    }

    @PostMapping("/update")
    public RestBean<Void> update(@RequestBody CheckProjects checkProjects) {
        checkProjectsService.updateById(checkProjects);
        return RestBean.success();
    }
}
