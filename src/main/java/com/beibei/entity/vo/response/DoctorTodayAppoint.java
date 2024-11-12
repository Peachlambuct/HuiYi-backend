package com.beibei.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorTodayAppoint {
    private int appointId;
    private int patientId;
    private int timeId;
    private String patientName;
    private String date;
    private int age;
    private int year;
    private int month;
    private int day;
    private LocalDateTime birthday;
}
