package com.example.medcare.controller;


import com.example.medcare.dto.AppointmentDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.service.ScheduleAppointmentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor //lombok annotation to create a constructor with all the required fields
@Getter

@CrossOrigin
public class AppointmentController {

    private final ScheduleAppointmentService scheduleAppointmentService;

    @PostMapping("/schedule")
    public ResponseEntity<Object> scheduleAppointment(
            @RequestBody AppointmentDTO requestForAppointment) {

        return scheduleAppointmentService.scheduleAppointment(requestForAppointment);       }
}
