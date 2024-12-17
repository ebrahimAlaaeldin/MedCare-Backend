package com.example.medcare.service;

import com.example.medcare.dto.AppointmentDTO;
import com.example.medcare.dto.CancelDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.entities.Appointment;
import com.example.medcare.repository.AppointmentRepository;
import com.example.medcare.repository.PatientRepository;
import com.example.medcare.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public ResponseMessageDto scheduleAppointment(AppointmentDTO appointmentDTO) {

        Appointment appointment = new Appointment();

        //check if patient exists
        var patient = patientRepository.findById(appointmentDTO.getPatientId());

        if (patient.isEmpty()) {

            return ResponseMessageDto.builder()
                    .message("Patient Not Found")
                    .success(false)
                    .statusCode(401)
                    .build();
        }


        //check if doctor exists
        var doctor = doctorRepository.findById(appointmentDTO.getDoctorId());

        if (doctor.isEmpty()) {

            return ResponseMessageDto.builder()
                    .message("Doctor Not Found")
                    .success(false)
                    .statusCode(401)
                    .build();
        }

        //check if appointment time is available
        boolean existsAnAppointment = appointmentRepository.existsByDoctorIdAndAppointmentTime(appointmentDTO.getDoctorId(), appointmentDTO.getAppointmentTime());

        if (existsAnAppointment) {

            return ResponseMessageDto.builder()
                    .message("Appointment Time Not Available")
                    .success(false)
                    .statusCode(401)
                    .build();
        }

        //save appointment
        appointment.setPatient(patient.get());
        appointment.setDoctor(doctor.get());
        appointment.setAppointmentTime(appointmentDTO.getAppointmentTime());
        appointment.setCancelled(false);
        appointment.setConfirmed(false);

        //save to database
        appointmentRepository.save(appointment);

        return ResponseMessageDto.builder()
                .message("Appointment Scheduled Successfully")
                .success(true)
                .statusCode(200)
                .build();

    }
}
