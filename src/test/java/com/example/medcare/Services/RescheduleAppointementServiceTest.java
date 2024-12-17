package com.example.medcare.Services;

import com.example.medcare.dto.AppointmentDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.entities.Appointment;
import com.example.medcare.entities.Doctor;
import com.example.medcare.repository.AppointmentRepository;
import com.example.medcare.service.RescheduleAppointementService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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

        // Set up existing appointment
        existingAppointment = new Appointment();
        existingAppointment.setAppointmentId(1);
        Doctor doctor = new Doctor();
        doctor.setId(2);
        existingAppointment.setDoctor(doctor);
        existingAppointment.setAppointmentTime("2025-12-16 12:00:00");

        // Set up new appointment DTO
        newAppointmentDTO = new AppointmentDTO();
        newAppointmentDTO.setDoctorId(2);
        newAppointmentDTO.setAppointmentTime("2025-12-17 12:00:00");
        newAppointmentDTO.setAppointmentId(1);
    }

    @Test
    void testRescheduleAppointment_Success() {
        // Mock repository behavior
        when(appointmentRepository.findByAppointmentId(1)).thenReturn(existingAppointment);
        when(appointmentRepository.existsByDoctorIdAndAppointmentTime(2, "2025-12-17 12:00:00")).thenReturn(false);

        // Call the service method
        ResponseEntity<Object> response = rescheduleAppointementService.rescheduleAppointment(newAppointmentDTO);

        // Assertions
        assertThat(response).isNotNull();
        assertThat(response.getBody().toString()).contains("Appointment Rescheduled Successfully");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

    }

    @Test
    void testRescheduleAppointment_TimeNotAvailable() {
        // Mock repository behavior
        when(appointmentRepository.findByAppointmentId(1)).thenReturn(existingAppointment);
        when(appointmentRepository.existsByDoctorIdAndAppointmentTime(2, "2025-12-17 12:00:00")).thenReturn(true);

        // Call the service method
        ResponseEntity<Object> response = rescheduleAppointementService.rescheduleAppointment(newAppointmentDTO);

        // Assertions
        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(409);
        assertThat(response.getBody().toString()).contains("Appointment Time Not Available");


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
