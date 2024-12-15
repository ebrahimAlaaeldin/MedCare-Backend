package com.example.medcare.service;

import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.dto.TokenThirdPartyDto;
import org.springframework.stereotype.Service;

import com.example.medcare.entities.User;
import com.example.medcare.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import com.example.medcare.config.*;
@Service
@RequiredArgsConstructor
public class ThirdPartyService {
    private UserRepository userRepository;
    private final JwtService jwtService;

    public ResponseMessageDto thirdPartyLogin(TokenThirdPartyDto token) throws Exception {
        try {
            String email = jwtService.extractEmail(token.getToken());
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return ResponseMessageDto.builder()
                    .message("User found")
                    .success(true)
                    .statusCode(200)
                    .data(user)
                    .build();
        } catch (Exception e) {
            return ResponseMessageDto.builder()
                    .message(e.getMessage())
                    .success(false)
                    .statusCode(404)
                    .data(jwtService.extractEmail(token.getToken()))
                    .build();
        }
    }




}
