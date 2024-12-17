package com.example.medcare.service;

import com.example.medcare.dto.CancelDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelAppointmentService {
    private final AppointmentRepository appointmentRepository;
    public ResponseEntity<Object> cancelAppointment(CancelDTO cancelDTO){

        //check if appointment exists
        var appointment = appointmentRepository.findById(cancelDTO.getAppointmentId());

        if(appointment.isEmpty()){
            return ResponseEntity.status(404).body(
                    ResponseMessageDto.builder()
                            .message("Appointment Not Found")
                            .success(false)
                            .statusCode(404)
                            .build());
        }

        //cancel appointment
        appointment.get().setCancelled(true);

        //save to database
        appointmentRepository.save(appointment.get());
        return ResponseEntity.ok().body(
                ResponseMessageDto.builder()
                        .message("Appointment Cancelled Successfully")
                        .success(true)
                        .statusCode(200)
                        .build());
    }
}
