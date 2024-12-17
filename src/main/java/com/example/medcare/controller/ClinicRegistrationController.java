package com.example.medcare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.medcare.dto.ClinicDTO;
import com.example.medcare.dto.ResponseDTO;
import com.example.medcare.service.ClinicRegistrationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clinic")
@RequiredArgsConstructor
public class ClinicRegistrationController {

    private final ClinicRegistrationService clinicRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerClinic(@RequestBody ClinicDTO request) {
        return clinicRegistrationService.registerClinic(request);
    }


    @PostMapping("/register/clinic-admin")
    public ResponseEntity<ResponseDTO> registerClinicAndAdmin(@RequestBody ClinicDTO request) {
        return clinicRegistrationService.registerClinicandAdminRegistration(request);
    }




}
