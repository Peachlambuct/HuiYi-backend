package com.beibei.controller;

import com.beibei.entity.RestBean;
import com.beibei.entity.vo.request.RegisterVO;
import com.beibei.service.UsersService;
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
@RequestMapping("/api/users")
public class UsersController {
    @Resource
    private UsersService usersService;

    @PostMapping("/register")
    public RestBean<Void> register(@RequestBody RegisterVO vo) throws Exception {
        usersService.register(vo);
        return RestBean.success();
    }
}
