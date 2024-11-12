package com.beibei.controller;

import com.beibei.entity.RestBean;
import com.beibei.entity.vo.request.CheckVO;
import com.beibei.service.ChecksService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@RestController
@RequestMapping("/api/check")
public class ChecksController {
    @Resource
    private ChecksService checksService;

    @PostMapping("/add")
    public RestBean<Void> addCheck(@RequestBody CheckVO checkVO) {
        checksService.saveCheck(checkVO);
        return RestBean.success();
    }
}
