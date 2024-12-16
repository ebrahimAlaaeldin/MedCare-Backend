package com.example.medcare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.medcare.dto.AppointmentDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.service.RescheduleAppointementService;
import com.example.medcare.service.ScheduleAppointmentService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor // lombok annotation to create a constructor with all the required fields
@Getter
@CrossOrigin
public class ScheduleController {

    private final ScheduleAppointmentService scheduleAppointmentService;
    private final RescheduleAppointementService rescheduleAppointementService;

    @PostMapping("/schedule")
    public ResponseEntity<ResponseMessageDto> scheduleAppointment(
            @RequestBody AppointmentDTO requestForAppointment) {

        // Schedule an appointment
        return ResponseEntity.ok(scheduleAppointmentService.scheduleAppointment(requestForAppointment));
    }
    @PostMapping("/reschedule")
    public ResponseEntity<Object> rescheduleAppointment(
            @RequestBody AppointmentDTO newAppointmentTime) {


        return rescheduleAppointementService.rescheduleAppointment(newAppointmentTime);
    }
}
