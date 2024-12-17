package com.example.medcare.service;


import com.example.medcare.config.JwtService;
import com.example.medcare.dto.AuthenticationRequest;
import com.example.medcare.dto.ResponseDTO;

import com.example.medcare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
        

    public Object authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())

            );
        }
        catch (Exception e) {
            Object response = ResponseDTO.builder()
                    .message("Invalid credentials")
                    .success(false)
                    .statusCode(401)
                    .build();

            //log.error("Invalid credentials", e);
            return response;
        }
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Map<String, Object> claims = Map.of("role", user.getRole().toString(),
            "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "email", user.getEmail(),
                "username", user.getUsername()
        )
                ;
        var token = jwtService.generateToken(claims, user);

        return ResponseDTO.builder()
                .message("Authentication successful")
                .success(true)
                .statusCode(200)
                .data(token)
                .build();


    }





}
