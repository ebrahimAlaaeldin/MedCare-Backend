package com.example.medcare.service;

import com.example.medcare.config.JwtService;
import com.example.medcare.dto.*;
import com.example.medcare.entities.ForgotPassword;
import com.example.medcare.repository.ForgotPasswordRepository;
import com.example.medcare.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

@Service
@AllArgsConstructor
public class ForgetResetPasswordService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final EmailService emailService;


    public ResponseMessageDto resetPassword(ResetPasswordDto input, String token) {
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

    public ResponseMessageDto forgetPassword(ForgetPassEmail email) {
        try {

            var user = userRepository.findByEmail(email.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            // create random pinNumber
            int otp = otpGenerator();

            // send email to user
            MailBody mailBody = MailBody.builder()
                    .to(user.getEmail())
                    .subject("OTP For Forgot Password request")
                    .body("Your password reset pin is: " + otp)
                    .build();

            ForgotPassword fp = ForgotPassword.builder()
                    .otp(otp)
                    .expirationTime(new Date(System.currentTimeMillis() + 600_000)) // 10 minutes
                    .user(user)
                    .build();

            emailService.setSimpleMessage(mailBody);
            forgotPasswordRepository.save(fp);


            return ResponseMessageDto.builder()
                    .message("Password OTP  sent to your email")
                    .statusCode(200)
                    .success(true)
                    .data(null)
                    .build();
        } catch (Exception e) {
            return ResponseMessageDto.builder()
                    .message("Email not found")
                    .statusCode(400)
                    .success(false)
                    .data(null)
                    .build();
        }
    }

    private int otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }

    public ResponseMessageDto validatePin(ValidateOTPDto otpValidationRequest) {
        try {
            var user = userRepository.findByEmail(otpValidationRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otpValidationRequest.getOtp(), user)
                    .orElseThrow(() -> new RuntimeException("OTP not found"));

            if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
                forgotPasswordRepository.delete(fp);
                return ResponseMessageDto.builder()
                        .message("OTP has expired")
                        .statusCode(400)
                        .success(false)
                        .data(null)
                        .build();
            }

            if (fp.getExpirationTime().before(new Date())) {
                return ResponseMessageDto.builder()
                        .message("OTP has expired")
                        .statusCode(400)
                        .success(false)
                        .data(null)
                        .build();
            }


            return ResponseMessageDto.builder()
                    .message("Pin is valid")
                    .statusCode(200)
                    .success(true)
                    .data(null)
                    .build();
        } catch (Exception e) {
            if (e.getMessage().equals("OTP not found")) {
                return ResponseMessageDto.builder()
                        .message("Invalid OTP")
                        .statusCode(400)
                        .success(false)
                        .data(null)
                        .build();
            }
            else {
                return ResponseMessageDto.builder()
                        .message("Failed to validate OTP")
                        .statusCode(400)
                        .success(false)
                        .data(null)
                        .build();
            }
        }

    }


    public ResponseMessageDto changePassword(ChangePasswordDto newUser){
        try {
            // Find the user by username
            var user = userRepository.findByEmail(newUser.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Update the user's password
            user.setPassword(passwordEncoder.encode(newUser.getNewPassword()));
            userRepository.save(user);
            return ResponseMessageDto.builder()
                    .message("Password changed successfully")
                    .statusCode(200)
                    .success(true)
                    .data(null)
                    .build();
        } catch (Exception e) {
            return ResponseMessageDto.builder()
                    .message("Failed to change password")
                    .statusCode(400)
                    .success(false)
                    .data(null)
                    .build();
        }
    }
}
