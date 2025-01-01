package com.example.medcare.service;

import java.util.List;

import org.eclipse.angus.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.medcare.dto.PaymentDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.entities.Payment;
import com.example.medcare.repository.AppointmentRepository;
import com.example.medcare.repository.paymentRepository;

import ch.qos.logback.core.status.Status;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class paymentService {
    @Autowired
    private final paymentRepository paymentRepository;

    @Autowired
    private final AppointmentRepository appointmentRepository;

    public ResponseEntity<Object> createPayment(int appointmentId, String paymentMethod, int amount) {
        try{
        Payment payment = new Payment();
        payment.setAppointment(appointmentRepository.findById(appointmentId).get());
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(amount);
        payment.setStatus(Payment.PaymentStatus.PAID);
        paymentRepository.save(payment);  
        return ResponseEntity.ok().body(ResponseMessageDto.builder()
        .message("Payment Successful")
        .success(true)
        .statusCode(200)
        .build());
        }catch(Exception e){
            return ResponseEntity.status(404)
            .body(ResponseMessageDto.builder()
            .message("Payment Failed")
            .success(false)
            .statusCode(404)
            .build());
        }
    }

    public ResponseEntity<Object> deletePayment(int id) {
        try {
            paymentRepository.deleteById(id);
            return ResponseEntity.ok().body(ResponseMessageDto.builder()
            .message("Payment Deleted")
            .success(true)
            .statusCode(200)
            .build());
        } catch (Exception e) {
            return ResponseEntity.status(404)
            .body(ResponseMessageDto.builder()
            .message("Payment Not Found")
            .success(false)
            .statusCode(404)
            .build());
        }

    }

    
}
