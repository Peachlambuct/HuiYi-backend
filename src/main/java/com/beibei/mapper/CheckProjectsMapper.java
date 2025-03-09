package com.beibei.mapper;

import com.beibei.entity.dto.CheckProjects;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.beibei.entity.vo.response.CheckProjectStatsVO;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Peachlambuct
 * @since 2024-11-09
 */
@Mapper
public interface CheckProjectsMapper extends BaseMapper<CheckProjects> {

    /**
     * 获取检查项目统计
     */
    @Select("SELECT COUNT(*) AS total, " +
            "(SELECT cp.name FROM check_projects cp " +
            "LEFT JOIN checks c ON cp.id = c.check_project_id " +
            "WHERE cp.deleted_at IS NULL " +
            "GROUP BY cp.id, cp.name " +
            "ORDER BY COUNT(c.id) DESC " +
            "LIMIT 1) AS most_popular " +
            "FROM check_projects " +
            "WHERE deleted_at IS NULL")
    CheckProjectStatsVO getCheckProjectStats();

}
