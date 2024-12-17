package com.example.medcare.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.medcare.dto.AppointmentDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RescheduleAppointementService {
    private final AppointmentRepository appointmentRepository;

    public ResponseEntity<Object> rescheduleAppointment(AppointmentDTO newAppointmentTime) {
        var  appointment = appointmentRepository.findByAppointmentId(newAppointmentTime.getAppointmentId());
        if(appointment==null){
            return ResponseEntity.status(404).body(ResponseMessageDto.builder()
                    .message("Appointment Not Found")
                    .success(false)
                    .statusCode(404)
                    .build());
        }


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime appointmentDateTime = LocalDateTime.parse(newAppointmentTime.getAppointmentTime(), formatter);
        boolean existsAnAppointment = appointmentRepository
                .existsByDoctorUsernameAndAppointmentDateTime(appointment.getDoctor().getUsername(),appointmentDateTime );
                    if (!existsAnAppointment) {
                    appointment.setAppointmentDateTime(appointmentDateTime);
                    appointmentRepository.save(appointment);
                    return ResponseEntity.ok(ResponseMessageDto.builder()
                            .message("Appointment Rescheduled Successfully")
                            .success(true)
                            .statusCode(200)
                            .build());
        }



        return ResponseEntity.status(409).body(ResponseMessageDto.builder()
                .message("Appointment Time Not Available")
                .success(false)
                .statusCode(409)
                .build());
    }
    
}
