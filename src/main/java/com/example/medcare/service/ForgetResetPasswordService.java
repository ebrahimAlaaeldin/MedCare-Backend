package com.example.medcare.service;

import com.example.medcare.config.JwtService;
import com.example.medcare.dto.AuthenticationRequest;
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

    public ResponseMessageDto forgetPassword(String email){
        try{
            var user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            // create random pinNumber
            String pinNumber = String.valueOf((int) (Math.random() * 10000)+555);
            user.setPinNumber(pinNumber);
            userRepository.save(user);
            // send email to user

            return ResponseMessageDto.builder()
                    .message("Password reset link sent to your email")
                    .statusCode(200)
                    .success(true)
                    .data(null)
                    .build();
        }catch (Exception e){
            return ResponseMessageDto.builder()
                    .message("Failed to send password reset link")
                    .statusCode(400)
                    .success(false)
                    .data(null)
                    .build();
        }
    }
    public ResponseMessageDto validatePin(String pin){
        try{
            var user = userRepository.findByPinNumber(pin)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return ResponseMessageDto.builder()
                    .message("Pin is valid")
                    .statusCode(200)
                    .success(true)
                    .data(null)
                    .build();
        }catch (Exception e){
            return ResponseMessageDto.builder()
                    .message("Invalid pin")
                    .statusCode(400)
                    .success(false)
                    .data(null)
                    .build();
        }
    }
    public ResponseMessageDto changePassword(AuthenticationRequest newUser){
        try{
            // Find the user by username
            var user = userRepository.findByUsername(newUser.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Update the user's password
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
            userRepository.save(user);
            return ResponseMessageDto.builder()
                    .message("Password changed successfully")
                    .statusCode(200)
                    .success(true)
                    .data(null)
                    .build();
        }catch (Exception e){
            return ResponseMessageDto.builder()
                    .message("Failed to change password")
                    .statusCode(400)
                    .success(false)
                    .data(null)
                    .build();
        }
    }
}
