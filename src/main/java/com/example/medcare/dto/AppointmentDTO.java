package com.example.medcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AppointmentDTO {
    private Integer patientId;
    private Integer doctorId;
    private String appointmentTime;
    private boolean isConfirmed;
    private boolean isCancelled;

}
