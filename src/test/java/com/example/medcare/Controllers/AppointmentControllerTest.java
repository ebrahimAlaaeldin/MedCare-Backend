package com.example.medcare.Controllers;

import com.example.medcare.controller.AppointmentController;
import com.example.medcare.dto.AppointmentDTO;
import com.example.medcare.dto.CancelDTO;
import com.example.medcare.service.CancelAppointmentService;
import com.example.medcare.service.RescheduleAppointementService;
import com.example.medcare.service.ScheduleAppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AppointmentControllerTest {

    @Mock
    private ScheduleAppointmentService scheduleAppointmentService;

    @Mock
    private CancelAppointmentService cancelAppointmentService;

    @Mock
    private RescheduleAppointementService rescheduleAppointementService;

    @InjectMocks
    private AppointmentController appointmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void scheduleAppointment_ShouldReturnSuccessResponse() {
        // Arrange
        AppointmentDTO request = new AppointmentDTO();
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Appointment Scheduled Successfully");
        when(scheduleAppointmentService.scheduleAppointment(any(AppointmentDTO.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<Object> actualResponse = appointmentController.scheduleAppointment(request);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void cancelAppointment_ShouldReturnSuccessResponse() {
        // Arrange
        CancelDTO request = new CancelDTO(1);
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Appointment Cancelled Successfully");
        when(cancelAppointmentService.cancelAppointment(any(CancelDTO.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<Object> actualResponse = appointmentController.cancelAppointment(request);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void rescheduleAppointment_ShouldReturnSuccessResponse() {
        // Arrange
        AppointmentDTO request = new AppointmentDTO();
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok("Appointment Rescheduled Successfully");
        when(rescheduleAppointementService.rescheduleAppointment(any(AppointmentDTO.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<Object> actualResponse = appointmentController.rescheduleAppointment(request);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }
}

