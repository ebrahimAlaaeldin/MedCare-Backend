package com.example.medcare.service;

import com.example.medcare.config.JwtService;
import com.example.medcare.dto.ForgetPasswordDto;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.PrimitiveIterator;

@Service
@AllArgsConstructor
public class ForgetResetPasswordService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public ResponseMessageDto resetPassword(ForgetPasswordDto input, String token) {
        // Reset the password
        var username = jwtService.extractUsername(token);
        try {
            // Find the user by username
            var user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Update the user's password
            user.setPassword(passwordEncoder.encode(input.getNewPassword()));
            userRepository.save(user);
            return ResponseMessageDto.builder()
                    .message("Password reset successfully")
                    .statusCode(200)
                    .success(true)
                    .data(null)
                    .build();

        } catch (Exception e) {
            return ResponseMessageDto.builder()
                    .message("Failed to reset password")
                    .statusCode(400)
                    .success(false)
                    .data(null)
                    .build();
        }



    }
}
