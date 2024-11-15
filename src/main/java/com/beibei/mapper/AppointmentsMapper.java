package com.beibei.mapper;

import com.beibei.entity.dto.Appointments;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.beibei.entity.vo.response.AppointmentCardVO;
import com.beibei.entity.vo.response.LastAppointmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

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
    @Select("SELECT appointments.id, doctors.name as doctor_name, doctors.job_title as doctor_title, doctors.job_type as doctor_type, " +
            "users.avatar as doctor_avatar, appointments.year, appointments.month, appointments.day, appointments.time_id, appointments.status " +
            "FROM appointments " +
            "LEFT JOIN doctors ON doctors.id = appointments.doctor_id " +
            "LEFT JOIN users ON users.id = doctors.user_id " +
            "WHERE appointments.deleted_at IS NULL AND appointments.patient_id = #{userId}")
    List<AppointmentCardVO> getAppointmentCardsByUserId(@Param("userId") Long userId);

    @Select("SELECT appointments.id, doctors.name as doctor_name, doctors.job_title as doctor_title, doctors.job_type as doctor_type, " +
            "users.avatar as doctor_avatar, appointments.year, appointments.month, appointments.day, appointments.time_id, appointments.status " +
            "FROM appointments " +
            "LEFT JOIN doctors ON doctors.id = appointments.doctor_id " +
            "LEFT JOIN users ON users.id = doctors.user_id " +
            "WHERE appointments.created_at BETWEEN #{from} AND #{to} " +
            "AND appointments.deleted_at IS NULL " +
            "AND appointments.patient_id = #{patientId} " +
            "AND appointments.status = #{status}")
    List<AppointmentCardVO> queryAppointments(@Param("from") LocalDateTime from,
                                              @Param("to") LocalDateTime to,
                                              @Param("patientId") Long patientId,
                                              @Param("status") int status);

    @Select("SELECT doctors.name as doctor_name, doctors.job_type as doctor_type, users.avatar as doctor_img, " +
            "appointments.updated_at as update_time, appointments.year, appointments.month, appointments.day, appointments.time_id " +
            "FROM appointments " +
            "LEFT JOIN doctors ON doctors.id = appointments.doctor_id " +
            "LEFT JOIN users ON users.id = doctors.user_id " +
            "WHERE appointments.patient_id = #{patientId} " +
            "ORDER BY appointments.created_at DESC " +
            "LIMIT 1")
    LastAppointmentVO getLatestAppointmentByPatientId(@Param("patientId") Long patientId);
}
