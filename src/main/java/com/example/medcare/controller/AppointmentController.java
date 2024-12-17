package com.example.medcare.controller;


import com.example.medcare.dto.AppointmentDTO;
import com.example.medcare.dto.CancelDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.service.CancelAppointmentService;
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
    private final CancelAppointmentService cancelappointmentService;


    @PostMapping("/schedule")
    public ResponseEntity<Object> scheduleAppointment(
            @RequestBody AppointmentDTO requestForAppointment) {

        return scheduleAppointmentService.scheduleAppointment(requestForAppointment);       }
  
    @PostMapping("/cancel")
    public ResponseEntity<Object> cancelAppointment(
            @RequestBody CancelDTO requestForCancelations) {
        // Cancel an appointment
        return cancelappointmentService.cancelAppointment(requestForCancelations);
    }
}
