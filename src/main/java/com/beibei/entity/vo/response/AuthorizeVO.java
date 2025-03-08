package com.beibei.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * 登录验证成功的用户信息响应
 */
@Data
public class AuthorizeVO {
    private Long id;
    private String username;
    private String userEmail;
    private String token;
    private String role;
    private boolean status;
    private Date expire;
}
