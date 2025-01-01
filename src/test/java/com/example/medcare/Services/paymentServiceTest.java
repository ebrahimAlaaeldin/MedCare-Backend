package com.example.medcare.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.entities.Appointment;
import com.example.medcare.entities.Payment;
import com.example.medcare.repository.AppointmentRepository;
import com.example.medcare.repository.paymentRepository;
import com.example.medcare.service.paymentService;

class paymentServiceTest {

    @Mock
    private paymentRepository paymentRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private paymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePayment_Success() {
        // Mock input
        int appointmentId = 1;
        String paymentMethod = "Credit Card";
        int amount = 500;

        // Mock dependencies
        Appointment mockAppointment = new Appointment();
        mockAppointment.setId(appointmentId);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(mockAppointment));

        Payment mockPayment = new Payment();
        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);

        // Call the service method
        ResponseEntity<Object> response = paymentService.createPayment(appointmentId, paymentMethod, amount);

        // Verify and assert
        assertEquals(200, ((ResponseMessageDto) response.getBody()).getStatusCode());
        assertEquals("Payment Successful", ((ResponseMessageDto) response.getBody()).getMessage());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testCreatePayment_Failure() {
        // Mock input
        int appointmentId = 1;
        String paymentMethod = "Credit Card";
        int amount = 500;

        // Mock dependencies
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        // Call the service method
        ResponseEntity<Object> response = paymentService.createPayment(appointmentId, paymentMethod, amount);

        // Verify and assert
        assertEquals(404, ((ResponseMessageDto) response.getBody()).getStatusCode());
        assertEquals("Payment Failed", ((ResponseMessageDto) response.getBody()).getMessage());
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testDeletePayment_Success() {
        // Mock input
        int paymentId = 1;

        // Call the service method
        ResponseEntity<Object> response = paymentService.deletePayment(paymentId);

        // Verify and assert
        assertEquals(200, ((ResponseMessageDto) response.getBody()).getStatusCode());
        assertEquals("Payment Deleted", ((ResponseMessageDto) response.getBody()).getMessage());
        verify(paymentRepository, times(1)).deleteById(paymentId);
    }

    @Test
    void testDeletePayment_Failure() {
        // Mock input
        int paymentId = 1;

        // Mock dependencies
        doThrow(new RuntimeException("Payment Not Found")).when(paymentRepository).deleteById(paymentId);

        // Call the service method
        ResponseEntity<Object> response = paymentService.deletePayment(paymentId);

        // Verify and assert
        assertEquals(404, ((ResponseMessageDto) response.getBody()).getStatusCode());
        assertEquals("Payment Not Found", ((ResponseMessageDto) response.getBody()).getMessage());
        verify(paymentRepository, times(1)).deleteById(paymentId);
    }
}
