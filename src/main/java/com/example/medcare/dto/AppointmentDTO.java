package com.example.medcare.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AppointmentDTO {
    private Integer patientId;
    private Integer doctorId;
    private String appointmentTime;
    private boolean isConfirmed;
    private boolean isCancelled;

}
