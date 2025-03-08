package com.beibei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.beibei.entity.dto.Cases;
import com.beibei.entity.vo.request.CaseQuery;
import com.beibei.entity.vo.request.ResponseCaseInfoVO;
import com.beibei.entity.vo.response.CaseInfoVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@Service
public interface CasesService extends IService<Cases> {
    void updateCase(ResponseCaseInfoVO vo);

    List<Cases> queryCase(CaseQuery query, Long userId);

    ResponseCaseInfoVO getResponseCaseInfo(Long id);

    /**
     * 获取病例详情
     *
     * @param caseId 病例ID
     * @return 病例详情VO
     */
    CaseInfoVO getCaseInfo(Long caseId);
}
