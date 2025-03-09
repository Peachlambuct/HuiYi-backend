package com.beibei.mapper;

import com.beibei.entity.dto.Patients;
import com.beibei.entity.vo.response.PatientStatsVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-03
 */
@Mapper
public interface PatientsMapper extends BaseMapper<Patients> {

    /**
     * 获取患者统计数据
     */
    @Select("SELECT " +
            "COUNT(*) AS total, " +
            "SUM(CASE WHEN DATE(created_at) = CURDATE() THEN 1 ELSE 0 END) AS today_new, " +
            "SUM(CASE WHEN created_at >= DATE_FORMAT(NOW(), '%Y-%m-01') THEN 1 ELSE 0 END) AS monthly_active " +
            "FROM patients " +
            "WHERE deleted_at IS NULL")
    PatientStatsVO getPatientStats();

    /**
     * 获取患者月度增长趋势
     */
    @Select("SELECT " +
            "DATE_FORMAT(created_at, '%Y-%m') AS month, " +
            "COUNT(*) AS patient_count " +
            "FROM patients " +
            "WHERE deleted_at IS NULL " +
            "AND created_at >= DATE_SUB(DATE_FORMAT(NOW(), '%Y-01-01'), INTERVAL 1 YEAR) " +
            "GROUP BY DATE_FORMAT(created_at, '%Y-%m') " +
            "ORDER BY month")
    List<Map<String, Object>> getMonthlyGrowthTrend();

}
