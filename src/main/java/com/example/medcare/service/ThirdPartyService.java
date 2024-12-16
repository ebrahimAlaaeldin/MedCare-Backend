package com.example.medcare.service;

import com.example.medcare.dto.ResponseDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.dto.TokenThirdPartyDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.medcare.entities.User;
import com.example.medcare.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import com.example.medcare.config.*;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ThirdPartyService {
    private UserRepository userRepository;
    private final JwtService jwtService;

    public ResponseEntity<Object> thirdPartyLogin(TokenThirdPartyDto token) throws Exception {
        try {

            // Extract the email from the token Google sends
            String email = jwtService.extractEmail(token.getToken());
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Map<String, Object> claims = Map.of("role", user.getRole().toString(),
                    "firstName", user.getFirstName(),
                    "lastName", user.getLastName(),
                    "email", user.getEmail(),
                    "username", user.getUsername()
            );
            String jwt = jwtService.generateToken(claims, user);
            return ResponseEntity.ok().body(ResponseDTO.builder()
                    .message("User found")
                    .statusCode(200)
                    .data(jwt)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                    .message("User not found")
                    .statusCode(400)
                    .data("Direct to registration page")
                    .build());
        }
    }




}
