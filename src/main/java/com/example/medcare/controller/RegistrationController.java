package com.example.medcare.controller;

import com.example.medcare.Authorization.AuthenticationResponse;
import com.example.medcare.dto.AuthenticationRequest;
import com.example.medcare.dto.ClinicAdminDTO;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.PatientDTO;
import com.example.medcare.dto.ResponseMessageDto;

import com.example.medcare.service.AuthenticateService;
import com.example.medcare.service.SignUpService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/authenticate")
@RequiredArgsConstructor 
@Getter
 
@CrossOrigin
public class
RegistrationController {

    private final SignUpService signUpService;
    private final AuthenticationProvider authenticationProvider;
    private final SignUpService regisrationService;
    private final AuthenticateService authenticateService;

    @PostMapping("/register/patient")
    public ResponseEntity<ResponseMessageDto> registerUser(@RequestBody PatientDTO request) {
        System.out.println("request = " + request);
        return signUpService.patientSignUp(request);
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<ResponseMessageDto> registerDoctor(@RequestBody DoctorDTO request) {

        return signUpService.doctorSignUp(request);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request) {
        return authenticateService.authenticate(request);
    }



    // if the user is logged in and its token is expired,
    // this endpoint will be called to refresh the token
    @GetMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(@RequestBody AuthenticationResponse token) {
        System.out.println(token);
        return authenticateService.refreshToken(token.getRefreshToken());
    }



    @PostMapping("/register/admin")
    public ResponseEntity<ResponseDTO> registerAdmin(@RequestBody ClinicAdminDTO request) {
        return signUpService.adminSignUp(request);
    }




}
