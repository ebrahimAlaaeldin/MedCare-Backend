package com.example.medcare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.medcare.dto.PaymentDTO;
import com.example.medcare.service.paymentService;


@RestController
@RequestMapping("/api/payment")
@CrossOrigin
@RequiredArgsConstructor
public class PaymentController {
    @Autowired
    private final paymentService paymentService;
    @PostMapping("/pay")
    public ResponseEntity<Object> postMethodName(@RequestBody PaymentDTO request) {
        return paymentService.createPayment(request.getAppointmentId(), request.getPaymentMethod(), request.getAmount());
    }
    @DeleteMapping("/deletePayment")
    public ResponseEntity<Object> deletePayment(@RequestBody PaymentDTO request) {
        return paymentService.deletePayment(request.getId());
    }
    
}
