package com.example.medcare.controller;


import com.example.medcare.dto.AppointmentDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.entities.Appointment;
import com.example.medcare.service.ScheduleAppointmentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ResponseMessageDto> scheduleAppointment(
            @RequestBody AppointmentDTO requestForAppointment) {

        // Schedule an appointment
        return ResponseEntity.ok(scheduleAppointmentService.scheduleAppointment(requestForAppointment));
    }
}
