package com.example.medcare.service;


import com.example.medcare.Authorization.AuthenticationResponse;
import com.example.medcare.config.JwtService;
import com.example.medcare.dto.AuthenticationRequest;
import com.example.medcare.dto.ResponseMessageDto;

import com.example.medcare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        

    public ResponseEntity<Object> authenticate(AuthenticationRequest request) {
        try {
            String username;
            User user;
            // handling the password in the front end
            if(request.getEmail() == null  && request.getUsername() != null){
                username = request.getUsername();
                user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));
            }
            else if(request.getUsername() == null && request.getEmail() != null){
                user = userRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));
                username = user.getUsername();
            }
            else {
                throw new IllegalArgumentException("Invalid request");
            }
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username,request.getPassword())

            );
        }
        catch (Exception e) {
            Object response = ResponseMessageDto.builder()
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

        return ResponseMessageDto.builder()
                .message("Authentication successful")
                .success(true)
                .statusCode(200)
                .data(token)
                .build();


    }





}
