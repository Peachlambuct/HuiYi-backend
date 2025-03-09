package com.beibei.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beibei.entity.RestBean;
import com.beibei.entity.dto.Users;
import com.beibei.entity.vo.request.RegisterVO;
import com.beibei.entity.vo.request.UpdatePasswordVO;
import com.beibei.entity.vo.response.AuthorizeVO;
import com.beibei.mapper.UsersMapper;
import com.beibei.service.SystemSettingsService;
import com.beibei.service.UsersService;
import com.beibei.utils.JwtUtils;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import com.beibei.utils.Const;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@RestController
@RequestMapping("/api/user")
public class UsersController {
    @Resource
    private UsersService usersService;
    @Resource
    private JwtUtils utils;
    @Resource
    private SystemSettingsService systemSettingsService;
    @Resource
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public RestBean<Void> register(@RequestBody RegisterVO vo) throws Exception {
        usersService.register(vo);
        return RestBean.success();
    }

    @PostMapping("/reg")
    public RestBean<AuthorizeVO> reg(@RequestBody RegisterVO vo) throws Exception {
        String setting = systemSettingsService.getSetting("allow_register", "true");
        Users user = usersService.getOne(new QueryWrapper<Users>().eq("username", vo.getUsername()));
        if (user != null) {
            if (!passwordEncoder.matches(vo.getPassword(), user.getPassword())) {
                // 密码不匹配，拒绝登录
                return RestBean.failure(401, "用户名或密码错误");
            }
            UserDetails userDetails = usersService.loadUserByUsername(vo.getUsername());
            String jwt = utils.createJwt(userDetails, user.getUsername(), user.getId());
            if (jwt == null) {
                return RestBean.forbidden("登录验证频繁，请稍后再试");
            } else {
                AuthorizeVO authorizeVO = user.asViewObject(AuthorizeVO.class, o -> o.setToken(jwt));
                authorizeVO.setExpire(utils.expireTime());
                return RestBean.success(authorizeVO);
            }
        } else if (setting.equals("true")) {
            usersService.register(vo);
        } else {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return RestBean.success();
    }

    @PostMapping("/update")
    public RestBean<Void> updatePassword(@RequestBody UpdatePasswordVO vo,
            @RequestAttribute(Const.ATTR_USER_ID) Integer userId) {
        Users user = usersService.getById(userId);
        if (Objects.equals(user.getPassword(), vo.getOldPassword())) {
            user.setPassword(vo.getNewPassword());
            usersService.updateById(user);
            return RestBean.success();
        } else {
            return RestBean.error(new Exception("原密码错误"));
        }
    }
}
