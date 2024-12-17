package com.example.medcare.service;


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
            return ResponseEntity.ok().body(ResponseMessageDto.builder()
                    .success(true)
                    .message("User logged in successfully")
                    .statusCode(200)
                    .data(jwt)
                    .build());
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseMessageDto.builder()
                    .success(false)
                    .message("Failed to log in")
                    .statusCode(400)
                    .data(e.getMessage())
                    .build());
        }
    }




}
