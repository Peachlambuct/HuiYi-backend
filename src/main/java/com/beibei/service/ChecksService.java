package com.beibei.service;

import com.beibei.entity.dto.Checks;
import com.baomidou.mybatisplus.extension.service.IService;
import com.beibei.entity.vo.request.CheckVO;
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
public interface ChecksService extends IService<Checks> {
    void saveCheck(CheckVO checkVO);
    void finishCheck(Long id);
}
