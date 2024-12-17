package com.example.medcare.Services;

import com.example.medcare.dto.CancelDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.entities.Appointment;
import com.example.medcare.repository.AppointmentRepository;
import com.example.medcare.service.CancelAppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CancelAppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private CancelAppointmentService cancelAppointmentService;

    public CancelAppointmentServiceTest() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void cancelAppointment_WhenAppointmentExists_ShouldCancelSuccessfully() {
        // Arrange
        int appointmentId = 1;
        CancelDTO cancelDTO = new CancelDTO(appointmentId);
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(appointmentId);
        appointment.setCancelled(false);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        // Act
        ResponseEntity<Object> response = cancelAppointmentService.cancelAppointment(cancelDTO);
        ResponseMessageDto responseBody = (ResponseMessageDto) response.getBody();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assert responseBody != null;
        assertEquals("Appointment Cancelled Successfully", responseBody.getMessage());
        assertEquals(true, responseBody.isSuccess());
        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(appointmentRepository, times(1)).save(appointment);
        assertEquals(true, appointment.isCancelled()); // Ensure the appointment is marked cancelled
    }

    @Test
    void cancelAppointment_WhenAppointmentDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        int appointmentId = 2;
        CancelDTO cancelDTO = new CancelDTO(appointmentId);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> response = cancelAppointmentService.cancelAppointment(cancelDTO);
        ResponseMessageDto responseBody = (ResponseMessageDto) response.getBody();

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assert responseBody != null;
        assertEquals("Appointment Not Found", responseBody.getMessage());
        assertEquals(false, responseBody.isSuccess());
        verify(appointmentRepository, times(1)).findById(appointmentId);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }
}

