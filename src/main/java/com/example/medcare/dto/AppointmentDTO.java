package com.example.medcare.dto;

import lombok.Data;

@Data
public class AppointmentDTO {
    private Integer appointmentId;
    private Integer patientId;
    private Integer doctorId;
    private String appointmentTime;
    private boolean isConfirmed;
    private boolean isCancelled;

}
