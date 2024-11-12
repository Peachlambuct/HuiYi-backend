package com.beibei.mapper;

import com.beibei.entity.dto.Checks;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.beibei.entity.vo.response.CheckInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@Mapper
public interface ChecksMapper extends BaseMapper<Checks> {
    @Select("SELECT checks.id, checks.created_at, checks.status, check_projects.name, check_projects.room " +
            "FROM checks " +
            "JOIN check_projects ON checks.check_project_id = check_projects.id " +
            "WHERE checks.patient_id = #{patientId} " +
            "ORDER BY IF(checks.status = '未检查', 1, 0), checks.created_at")
    List<CheckInfoVO> getCheckInfoByPatientId(Long patientId);

}
