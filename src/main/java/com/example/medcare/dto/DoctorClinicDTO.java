package com.example.medcare.dto;


import com.example.medcare.Enums.WeekDays;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorClinicDTO {

        private Integer doctorId;
        private String doctorUsername;
        private String doctorEmail;
        private String clinicName;
        private Integer clinicId;
        private WeekDays weekDay;
        private LocalTime startTime;
        private LocalTime endTime;

}
