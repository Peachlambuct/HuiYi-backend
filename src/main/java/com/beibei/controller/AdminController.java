package com.beibei.controller;

import com.beibei.entity.RestBean;
import com.beibei.entity.dto.CheckProjects;
import com.beibei.entity.dto.Users;
import com.beibei.entity.vo.request.AdminLoginVO;
import com.beibei.entity.vo.request.UpdateCheckProjectVO;
import com.beibei.entity.vo.response.AuthorizeVO;
import com.beibei.entity.vo.response.CountResponseVO;
import com.beibei.entity.vo.response.DoctorCard;
import com.beibei.entity.vo.response.PatientInfoVO;
import com.beibei.service.AdminService;
import com.beibei.utils.JwtUtils;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Resource
    private AdminService adminService;
    @Resource
    private JwtUtils utils;

    @GetMapping("/getkey")
    public RestBean<String> getKey() {
        return RestBean.success(adminService.getKey());
    }

    @PostMapping("/login")
    public RestBean<AuthorizeVO> login(@RequestBody AdminLoginVO vo) {
        if (!adminService.login(vo)) {
            return RestBean.error(new Exception("登录失败"));
        }
        Users user = new Users();
        user.setRole("admin");
        user.setUsername("admin");
        user.setId(10000L);
        UserDetails admin = User
                .withUsername("admin")
                .password("hakjwhdkjhajkshdkjahwkjdhakjshdkjawhkjd")
                .roles(user.getRole())
                .build();
        String jwt = utils.createJwt(admin, "admin", user.getId());
        if (jwt == null) {
            return RestBean.forbidden("登录验证频繁，请稍后再试");
        } else {
            AuthorizeVO authorizeVO = new AuthorizeVO();
            authorizeVO.setToken(jwt);
            authorizeVO.setExpire(utils.expireTime());
            authorizeVO.setUsername("admin");
            authorizeVO.setStatus(true);
            authorizeVO.setRole("admin");
            authorizeVO.setUserEmail("1479539484@qq.com");
            return RestBean.success(authorizeVO);
        }
    }

    @PostMapping("/add")
    public RestBean<String> add(@RequestBody CheckProjects checkProjects) {
        adminService.addCheckProject(checkProjects);
        return RestBean.success();
    }

    @PostMapping("/update")
    public RestBean<String> update(@RequestBody UpdateCheckProjectVO vo) {
        adminService.updateCheckProject(vo);
        return RestBean.success();
    }

    @GetMapping("/delete")
    public RestBean<String> delete(@RequestParam("id") Integer id) {
        adminService.deleteCheckProject(id);
        return RestBean.success();
    }

    @GetMapping("/info")
    public RestBean<List<CheckProjects>> info() {
        return RestBean.success(adminService.getCheckProjects());
    }

    @GetMapping("/count")
    public RestBean<CountResponseVO> count() {
        return RestBean.success(adminService.getCount());
    }

    @GetMapping("/doctor")
    public RestBean<List<DoctorCard>> doctor() {
        return RestBean.success(adminService.getDoctorInfo());
    }

    @GetMapping("/patient")
    public RestBean<List<PatientInfoVO>> patient() {
        return RestBean.success(adminService.getPatientInfo());
    }

    @GetMapping("/deldoctor")
    public RestBean<Void> delDoctor(@RequestParam("id") Long id) {
        adminService.deleteDoctor(id);
        return RestBean.success();
    }

    @GetMapping("/delpatient")
    public RestBean<Void> delPatient(@RequestParam("id") Long id) {
        adminService.deletePatient(id);
        return RestBean.success();
    }
}
