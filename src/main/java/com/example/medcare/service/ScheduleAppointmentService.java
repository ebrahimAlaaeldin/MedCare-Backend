package com.example.medcare.service;

import com.example.medcare.dto.AppointmentDTO;
import com.example.medcare.dto.CancelDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.entities.Appointment;
import com.example.medcare.repository.AppointmentRepository;
import com.example.medcare.repository.PatientRepository;
import com.example.medcare.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ScheduleAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;


    public ResponseEntity<Object> scheduleAppointment(AppointmentDTO appointmentDTO){
        
        if(appointmentDTO == null){
            return ResponseEntity.status(400).body(ResponseMessageDto.builder()
            .message("Invalid Request")
            .success(false)
            .statusCode(400)
            .build());
        }
        
        if(appointmentDTO.getDoctorUsername() == null || appointmentDTO.getPatientUsername() == null ||
                appointmentDTO.getAppointmentDate() == null || appointmentDTO.getAppointmentTime() == null){

            return ResponseEntity.badRequest().body(ResponseMessageDto.builder()
                    .message("Please provide all the required fields")
                    .success(false)
                    .statusCode(400)
                    .build());
        }



        Appointment appointment = new Appointment();

        //check if patient exists
        var patient = patientRepository.findByUsername(appointmentDTO.getPatientUsername());

        if (patient.isEmpty()) {

            return ResponseEntity.status(404).body(ResponseMessageDto.builder()
            .message("Patient Not Found")
            .success(false)
            .statusCode(404)
            .build());
        }


        //check if doctor exists
        var doctor = doctorRepository.findByUsername(appointmentDTO.getDoctorUsername());

        if (doctor.isEmpty()) {


            return ResponseEntity.status(404).body(ResponseMessageDto.builder()
            .message("Doctor Not Found")
            .success(false)
            .statusCode(404)
            .build());
        }

        LocalDate date = LocalDate.parse(appointmentDTO.getAppointmentDate());
        LocalTime time = LocalTime.parse(appointmentDTO.getAppointmentTime());

        LocalDateTime appointmentDateTime = LocalDateTime.of(date, time);
        
        //check if appointment time is available
        boolean existsAnAppointment = appointmentRepository.existsByDoctorUsernameAndAppointmentDateTime(
                appointmentDTO.getDoctorUsername(), appointmentDateTime);

        if (existsAnAppointment) {

            return ResponseEntity.status(409).body(ResponseMessageDto.builder()
            .message("This time slot is reserved.")
            .success(false)
            .statusCode(409)
            .build());
        }

        //save appointment
        appointment.setPatient(patient.get());
        appointment.setDoctor(doctor.get());
        appointment.setAppointmentDateTime(appointmentDateTime);
        appointment.setCancelled(false);
        appointment.setConfirmed(false);

        //save to database
        appointmentRepository.save(appointment);


        return ResponseEntity.ok().body(ResponseMessageDto.builder()
        .message("Appointment Scheduled Successfully")
        .success(true)
        .statusCode(200)
        .build());

    }
}
