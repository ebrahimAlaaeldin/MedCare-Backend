package com.example.medcare.controller;


import com.example.medcare.dto.AppointmentDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.entities.Appointment;
import com.example.medcare.service.ScheduleAppointmentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor //lombok annotation to create a constructor with all the required fields
@Getter

@CrossOrigin
public class ScheduleController {

    private final ScheduleAppointmentService scheduleAppointmentService;

    @PostMapping("/schedule")
    public ResponseEntity<Object> scheduleAppointment(
            @RequestBody AppointmentDTO requestForAppointment) {

        
        if(requestForAppointment.getPatientId() == null || requestForAppointment.getDoctorId() == null || requestForAppointment.getAppointmentTime() == null) {
            return ResponseEntity.badRequest().body(ResponseMessageDto.builder()
                    .message("Please provide all the required fields")
                    .success(false)
                    .statusCode(400)
                    .build());
        }

        return scheduleAppointmentService.scheduleAppointment(requestForAppointment);       }
}
