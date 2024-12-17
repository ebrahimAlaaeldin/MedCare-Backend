package com.example.medcare.service;

import com.example.medcare.dto.ResetPasswordDto;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.dto.TokenThirdPartyDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

    public ResponseMessageDto thirdPartyLogin(TokenThirdPartyDto token) throws Exception {
        try {

            // Extract the email from the token Google sends
            String email = jwtService.extractEmail(token.getToken());
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return ResponseMessageDto.builder()
                    .message("User found")
                    .success(true)
                    .message("User logged in successfully")
                    .statusCode(200)
                    .data(jwt)
                    .build());
        } catch (Exception e) {
            return ResponseMessageDto.builder()
                    .message(e.getMessage())
                    .success(false)
                    .message("Failed to log in")
                    .statusCode(400)
                    .data(e.getMessage())
                    .build());
        }
    }




}
