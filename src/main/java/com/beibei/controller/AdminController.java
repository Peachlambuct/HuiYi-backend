package com.beibei.controller;

import com.beibei.entity.RestBean;
import com.beibei.entity.vo.request.AdminLoginVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/admin")
public class AdminController {
    @GetMapping("/getkey")
    public RestBean<String> getKey(){
        return RestBean.success();
    }

    @PostMapping("/login")
    public RestBean<String> login(@RequestBody AdminLoginVO vo){
        return RestBean.success();
    }

    @PostMapping("/add")
    public RestBean<String> add(@RequestBody AdminLoginVO vo){
        return RestBean.success();
    }

    @PostMapping("/update")
    public RestBean<String> update(@RequestBody AdminLoginVO vo){
        return RestBean.success();
    }

    @GetMapping("/delete")
    public RestBean<String> delete(@RequestBody AdminLoginVO vo){
        return RestBean.success();
    }

    @GetMapping("/info")
    public RestBean<String> info(){
        return RestBean.success();
    }

    @GetMapping("/count")
    public RestBean<String> count(){
        return RestBean.success();
    }

    @GetMapping("/doctor")
    public RestBean<String> doctor(){
        return RestBean.success();
    }

    @GetMapping("/patient")
    public RestBean<String> patient(){
        return RestBean.success();
    }

    @GetMapping("/deldoctor")
    public RestBean<Void> delDoctor() {
        return RestBean.success();
    }

    @GetMapping("/delpatient")
    public RestBean<Void> delPatient() {
        return RestBean.success();
    }
}
