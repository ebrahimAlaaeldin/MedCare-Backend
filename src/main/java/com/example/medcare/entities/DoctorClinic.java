package com.example.medcare.entities;


import com.example.medcare.Enums.WeekDays;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Table(name = "doctor_clinic_schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class DoctorClinic

{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctorId;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinicId;

    @Enumerated(EnumType.STRING)
    private WeekDays weekDay;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime; // 9:00 A

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;


}
