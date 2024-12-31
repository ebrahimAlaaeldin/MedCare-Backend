package com.example.medcare.dto;


import com.example.medcare.entities.Appointment;
import lombok.*;

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
