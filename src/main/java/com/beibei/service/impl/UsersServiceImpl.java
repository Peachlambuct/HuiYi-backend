package com.beibei.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beibei.entity.dto.Users;
import com.beibei.entity.vo.request.RegisterVO;
import com.beibei.mapper.UsersMapper;
import com.beibei.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@Slf4j
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService, UserDetailsService {

    @Resource
    private PasswordEncoder encoder;

    @Override
    public Users findAccountByNameOrEmail(String str) {
        return this.getOne(new QueryWrapper<Users>().eq("username", str).or().eq("email", str));
    }

    @Override
    public void register(RegisterVO vo) throws Exception {
        if (StrUtil.isBlank(vo.getUsername()) || StrUtil.isBlank(vo.getPassword())) {
            throw new Exception("用户名和密码不能为空");
        }
        Users users = new Users();
        users.setUsername(vo.getUsername());
        users.setPassword(encoder.encode(vo.getPassword()));
        this.save(users);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StrUtil.isBlank(username)) {
            log.error("Username is null");
            throw new UsernameNotFoundException("Username cannot be null");
        }
        Users user = this.findAccountByNameOrEmail(username);
        if (user == null) {
            log.info("{} not found", username);
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return User
                .withUsername(username)
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
