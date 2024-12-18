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
    private Integer appointmentId;
    private String patientUsername;
    private String doctorUsername;
    private String appointmentDate;
    private String appointmentTime;
    private boolean isConfirmed;
    private boolean isCancelled;

}
