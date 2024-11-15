package com.beibei.mapper;

import com.beibei.entity.dto.Doctors;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.beibei.entity.vo.response.DoctorCard;
import com.beibei.entity.vo.response.DoctorTodayAppoint;
import com.beibei.entity.vo.response.NonFinishCase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
public interface DoctorsMapper extends BaseMapper<Doctors> {
    @Select("SELECT DISTINCT job_type FROM doctors")
    List<String> getJobTypes();

    @Select("SELECT appointments.id as appoint_id, appointments.patient_id, appointments.time_id, " +
            "patients.name as patient_name, patients.birthday, appointments.year, appointments.month, appointments.day " +
            "FROM appointments " +
            "LEFT JOIN patients ON patients.id = appointments.patient_id " +
            "WHERE appointments.doctor_id = #{doctorID} AND appointments.deleted_at IS NULL " +
            "ORDER BY appointments.year, appointments.month, appointments.day, appointments.time_id")
    List<DoctorTodayAppoint> getAppointmentsByDoctorId(Long doctorID);

    @Select("SELECT cases.id, patients.name as patient_name, birthday, cases.updated_at, sex " +
            "FROM cases " +
            "LEFT JOIN patients ON patients.id = cases.patient_id " +
            "WHERE doctor_id = #{doctorID} AND status = false")
    List<NonFinishCase> getCasesByDoctorIdAndStatus(Long doctorID);


    @Select("SELECT doctors.id, doctors.name, doctors.honor, doctors.job_title, doctors.job_type, doctors.phone, users.avatar AS img " +
            "FROM doctors " +
            "LEFT JOIN users ON users.id = doctors.user_id")
    List<DoctorCard> findAllDoctors();
}
