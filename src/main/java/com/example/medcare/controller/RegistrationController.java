package com.example.medcare.controller;


import com.example.medcare.Authorization.AuthenticationResponse;
import com.example.medcare.dto.AuthenticationRequest;
import com.example.medcare.dto.SignUpRequest;
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

import java.io.IOException;

@RestController
@RequestMapping("/api/authenticate")
@RequiredArgsConstructor //lombok annotation to create a constructor with all the required fields
@Getter
public class RegistrationController {

    private final SignUpService signUpService;
    private final AuthenticationProvider authenticationProvider;
    private final SignUpService regisrationService;
    private final AuthenticateService authenticateService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody SignUpRequest request) {
        System.out.println("request = " + request);
        return ResponseEntity.ok(signUpService.signUp(request));

    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticateService.authenticate(request));

    }

//    // if the user is logged in and its token is expired, this endpoint will be called to refresh the token
//    @PostMapping("/refresh-token")
//    public void refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException {
//        regisrationService.refreshToken(request, response);
//    }


}
