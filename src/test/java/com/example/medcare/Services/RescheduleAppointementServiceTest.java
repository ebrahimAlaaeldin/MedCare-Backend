package com.example.medcare.Services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.example.medcare.dto.AppointmentDTO;
import com.example.medcare.entities.Appointment;
import com.example.medcare.entities.Doctor;
import com.example.medcare.repository.AppointmentRepository;
import com.example.medcare.service.RescheduleAppointementService;

public class RescheduleAppointementServiceTest {

   @InjectMocks
   private RescheduleAppointementService rescheduleAppointementService;

   @Mock
   private AppointmentRepository appointmentRepository;

   private Appointment existingAppointment;
   private AppointmentDTO newAppointmentDTO;

   @BeforeEach
void setUp() {
    MockitoAnnotations.openMocks(this);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Set up existing appointment
    existingAppointment = new Appointment();
    existingAppointment.setAppointmentId(1);
    Doctor doctor = new Doctor();
    doctor.setId(2);
    doctor.setUsername("doctor");
    existingAppointment.setDoctor(doctor);
    existingAppointment.setAppointmentDateTime(LocalDateTime.parse("2025-12-17 12:00:00", formatter));

    // Set up new appointment DTO
    newAppointmentDTO = new AppointmentDTO();
    newAppointmentDTO.setDoctorUsername("doctor");
    newAppointmentDTO.setAppointmentTime("2025-12-17 12:00:00");
    newAppointmentDTO.setAppointmentId(1);
}


   @Test
   void testRescheduleAppointment_Success() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
       // Mock repository behavior
       when(appointmentRepository.findByAppointmentId(1)).thenReturn(existingAppointment);
        when(appointmentRepository.existsByDoctorUsernameAndAppointmentDateTime("doctor", LocalDateTime.parse("2025-12-17 12:00:00",formatter))).thenReturn(false);

       // Call the service method
       ResponseEntity<Object> response = rescheduleAppointementService.rescheduleAppointment(newAppointmentDTO);

       // Assertions
       assertThat(response).isNotNull();
       assertThat(response.getBody().toString()).contains("Appointment Rescheduled Successfully");
       assertThat(response.getStatusCodeValue()).isEqualTo(200);

   }

   @Test
   void testRescheduleAppointment_TimeNotAvailable() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
         // Mock repository behavior
         when(appointmentRepository.findByAppointmentId(1)).thenReturn(existingAppointment);
         when(appointmentRepository.existsByDoctorUsernameAndAppointmentDateTime("doctor", LocalDateTime.parse("2025-12-17 12:00:00",formatter))).thenReturn(true);
    
         // Call the service method
         ResponseEntity<Object> response = rescheduleAppointementService.rescheduleAppointment(newAppointmentDTO);
    
         // Assertions
         assertThat(response).isNotNull();
         assertThat(response.getBody().toString()).contains("Appointment Time Not Available");
         assertThat(response.getStatusCodeValue()).isEqualTo(409);

   }

   @Test
   void testRescheduleAppointment_AppointmentNotFound() {
       // Mock repository behavior for a non-existent appointment
       when(appointmentRepository.findByAppointmentId(1)).thenReturn(null);

       // Call the service method
       ResponseEntity<Object> response = rescheduleAppointementService.rescheduleAppointment(newAppointmentDTO);

       // Assertions
       assertThat(response).isNotNull();
       assertThat(response.getBody().toString()).contains("Appointment Not Found");
       assertThat(response.getStatusCodeValue()).isEqualTo(404);
   }
}
