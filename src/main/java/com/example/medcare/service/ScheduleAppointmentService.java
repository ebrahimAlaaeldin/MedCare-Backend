package com.example.medcare.service;

import com.example.medcare.dto.AppointmentDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.entities.Appointment;
import com.example.medcare.entities.Doctor;
import com.example.medcare.entities.Patient;
import com.example.medcare.entities.User;
import com.example.medcare.repository.AppointmentRepository;
import com.example.medcare.repository.PatientRepository;
import com.example.medcare.repository.DoctorRepository;
import com.example.medcare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.print.Doc;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ScheduleAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository  userRepository;

    protected ResponseEntity<Object> reserve(Patient patient, Doctor doctor, LocalDateTime appointmentDateTime){
        //check if appointment time is available
        boolean existsAnAppointment = appointmentRepository.existsByDoctorUsernameAndAppointmentDateTime(
                doctor.getUsername(), appointmentDateTime);

        if (existsAnAppointment) {

            return ResponseEntity.status(409).body(ResponseMessageDto.builder()
                    .message("This time slot is reserved.")
                    .success(false)
                    .statusCode(409)
                    .build());
        }
        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .isCancelled(false)
                .isConfirmed(false)
                .createdAt(LocalDateTime.now())
                .appointmentDateTime(appointmentDateTime)
                .build();

        //save to database
        appointmentRepository.save(appointment);
        return ResponseEntity.ok().body(ResponseMessageDto.builder()
                .message("Appointment Scheduled Successfully")
                .success(true)
                .statusCode(200)
                .build());

    }
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

        return reserve(patient.get(), doctor.get(), appointmentDateTime);


    }

    public ResponseEntity<Object> viewAppointmentsForCertainPatient(String patientUsername) {
        if (patientUsername == null || patientUsername.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(ResponseMessageDto.builder()
                    .message("Invalid Request: Username is null or empty.")
                    .success(false)
                    .statusCode(400)
                    .build());
        }

        Optional<User> user = userRepository.findByUsername(patientUsername);
        if (!user.isPresent()) {
            return ResponseEntity.status(404).body(ResponseMessageDto.builder()
                    .message("Patient Not Found")
                    .success(false)
                    .statusCode(404)
                    .build());
        }

        List<Appointment> appointments = appointmentRepository.findByPatient_Username(patientUsername);
        List<AppointmentDTO> appointmentDTOs = new ArrayList<>();

        for(Appointment appointment : appointments){
            if (appointment.isCancelled()) {
                continue;
            }
            appointmentDTOs.add(AppointmentDTO.builder()
                    .appointmentId(appointment.getAppointmentId())
                    .patientUsername(appointment.getPatient().getUsername())
                    .doctorUsername(appointment.getDoctor().getUsername())
                    .appointmentDate(appointment.getAppointmentDateTime().toLocalDate().toString())
                    .appointmentTime(appointment.getAppointmentDateTime().toLocalTime().toString())
                    .isConfirmed(appointment.isConfirmed())
                    .isCancelled(appointment.isCancelled())
                    .build());
        }

        if (appointments.isEmpty()) {
            return ResponseEntity.ok(ResponseMessageDto.builder()
                    .message("No Appointments Found")
                    .success(true)
                    .statusCode(200)
                    .build());
        }

        return ResponseEntity.ok(ResponseMessageDto.builder()
                .message("Appointments for " + patientUsername)
                .success(true)
                .statusCode(200)
                .data(appointmentDTOs)
                .build());
    }
}
