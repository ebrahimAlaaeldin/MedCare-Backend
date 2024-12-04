package com.example.medcare.controller;

import com.example.medcare.dto.AuthenticationRequest;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.PatientDTO;
import com.example.medcare.dto.ResponseMessageDto;

import com.example.medcare.service.AuthenticateService;
import com.example.medcare.service.SignUpService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/authenticate")
@RequiredArgsConstructor //lombok annotation to create a constructor with all the required fields
@Getter
public class RegistrationController {

    private final SignUpService signUpService;
    private final AuthenticationProvider authenticationProvider;
    private final SignUpService regisrationService;
    private final AuthenticateService authenticateService;

    @PostMapping("/register/patient")
    public ResponseEntity<ResponseMessageDto> registerUser(@RequestBody PatientDTO request) {
        System.out.println("request = " + request);
        return ResponseEntity.ok(signUpService.patientSignUp(request));
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<ResponseMessageDto> registerDoctor(@RequestBody DoctorDTO request) {

        return ResponseEntity.ok(signUpService.doctorSignUp(request));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticateService.authenticate(request));
    }




}
