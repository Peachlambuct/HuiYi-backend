package com.beibei.service;

import com.beibei.entity.dto.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import com.beibei.entity.vo.request.RegisterVO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@Service
public interface UsersService extends IService<Users>, UserDetailsService {
    Users findAccountByNameOrEmail(String account);
    void register(RegisterVO vo) throws Exception;
}
