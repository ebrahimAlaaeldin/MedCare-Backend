package com.example.medcare.dto;


import com.example.medcare.Enums.WeekDays;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class ClinicDoctorAssignmentDTO {
    private String doctorUsername;
    private Integer clinicId;
    private Integer doctorId;
    private WeekDays weekDay;
    private String startTime;
    private String endTime;

}
