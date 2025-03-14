package com.beibei.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beibei.entity.dto.Checks;
import com.beibei.entity.vo.request.AddCheckVO;
import com.beibei.entity.vo.request.CheckVO;
import com.beibei.mapper.ChecksMapper;
import com.beibei.service.ChecksService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@Service
public class ChecksServiceImpl extends ServiceImpl<ChecksMapper, Checks> implements ChecksService {

    @Override
    public void saveCheck(CheckVO checkVO) {
        Checks checks = new Checks();
        BeanUtil.copyProperties(checkVO, checks);
        this.save(checks);
    }

    @Override
    public void finishCheck(Long id) {
        Checks checks = this.getById(id);
        checks.setStatus("已完成");
        this.updateById(checks);
    }

    @Override
    public void addCheck(AddCheckVO checkVO) {
        Checks checks = new Checks();
        BeanUtil.copyProperties(checkVO, checks);
        checks.setCheckProjectId(checkVO.getCheckId());
        this.saveOrUpdate(checks);
    }
}
