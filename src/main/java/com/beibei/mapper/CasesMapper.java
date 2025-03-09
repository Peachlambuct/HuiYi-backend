package com.beibei.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.beibei.entity.dto.Cases;
import com.beibei.entity.vo.response.MedicalRecordStatsVO;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@Mapper
public interface CasesMapper extends BaseMapper<Cases> {

    /**
     * 获取医疗记录统计
     */
    @Select("SELECT " +
            "COUNT(*) AS total, " +
            "SUM(CASE WHEN DATE(created_at) = CURDATE() THEN 1 ELSE 0 END) AS today_new, " +
            "(SELECT COUNT(*) FROM appointments WHERE deleted_at IS NULL) AS total_appointments " +
            "FROM cases " +
            "WHERE deleted_at IS NULL")
    MedicalRecordStatsVO getMedicalRecordStats();

}
