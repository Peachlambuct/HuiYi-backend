package com.beibei.mapper;

import com.beibei.entity.dto.Appointments;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.beibei.entity.vo.response.AppointmentCardVO;
import com.beibei.entity.vo.response.LastAppointmentVO;
import com.beibei.entity.vo.response.AppointmentStatsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
public interface AppointmentsMapper extends BaseMapper<Appointments> {
        @Select("SELECT appointments.id, doctors.name as doctor_name, doctors.job_title as doctor_title, doctors.job_type as doctor_type, "
                        +
                        "users.avatar as doctor_avatar, appointments.year, appointments.month, appointments.day, appointments.time_id, appointments.status "
                        +
                        "FROM appointments " +
                        "LEFT JOIN doctors ON doctors.id = appointments.doctor_id " +
                        "LEFT JOIN users ON users.id = doctors.user_id " +
                        "WHERE appointments.deleted_at IS NULL AND appointments.patient_id = #{userId}")
        List<AppointmentCardVO> getAppointmentCardsByUserId(@Param("userId") Long userId);

        @Select("SELECT appointments.id, doctors.name as doctor_name, doctors.job_title as doctor_title, doctors.job_type as doctor_type, "
                        +
                        "users.avatar as doctor_avatar, appointments.year, appointments.month, appointments.day, appointments.time_id, appointments.status "
                        +
                        "FROM appointments " +
                        "LEFT JOIN doctors ON doctors.id = appointments.doctor_id " +
                        "LEFT JOIN users ON users.id = doctors.user_id " +
                        "${ew.customSqlSegment}")
        List<AppointmentCardVO> queryAppointments(@Param("ew") Wrapper<Appointments> wrapper);

        @Select("SELECT doctors.name as doctor_name, doctors.job_type as doctor_type, users.avatar as doctor_img, " +
                        "appointments.updated_at as update_time, appointments.year, appointments.month, appointments.day, appointments.time_id "
                        +
                        "FROM appointments " +
                        "LEFT JOIN doctors ON doctors.id = appointments.doctor_id " +
                        "LEFT JOIN users ON users.id = doctors.user_id " +
                        "WHERE appointments.patient_id = #{patientId} " +
                        "ORDER BY appointments.created_at DESC " +
                        "LIMIT 1")
        LastAppointmentVO getLatestAppointmentByPatientId(@Param("patientId") Long patientId);

        /**
         * 获取今日预约统计
         */
        @Select("SELECT COUNT(*) as total, " +
                        "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as completed, " +
                        "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) as pending " +
                        "FROM appointments " +
                        "WHERE deleted_at IS NULL " +
                        "AND year = #{year} AND month = #{month} AND day = #{day}")
        AppointmentStatsVO getAppointmentStats(@Param("year") String year,
                        @Param("month") String month,
                        @Param("day") String day);

        /**
         * 获取每周预约分布
         */
        @Select("SELECT " +
                        "DAYOFWEEK(CONCAT(year, '-', month, '-', day)) AS day_of_week, " +
                        "COUNT(*) AS appointment_count " +
                        "FROM appointments " +
                        "WHERE deleted_at IS NULL " +
                        "AND CONCAT(year, '-', month, '-', day) BETWEEN #{startDate} AND #{endDate} " +
                        "GROUP BY day_of_week " +
                        "ORDER BY day_of_week")
        List<Map<String, Object>> getWeeklyDistribution(@Param("startDate") String startDate,
                        @Param("endDate") String endDate);
}
