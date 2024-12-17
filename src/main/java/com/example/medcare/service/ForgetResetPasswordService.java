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
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class ForgetResetPasswordService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final EmailService emailService;


    public ResponseDTO resetPassword(ResetPasswordDto input, String token) {
        // Reset the password
        var username = jwtService.extractUsername(token);
        try {
            // Find the user by username
            var user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Update the user's password
            user.setPassword(passwordEncoder.encode(input.getNewPassword()));
            userRepository.save(user);
            return ResponseDTO.builder()
                    .message("Password reset successfully")
                    .statusCode(200)
                    .success(true)
                    .data(null)
                    .build();

        } catch (Exception e) {
            return ResponseDTO.builder()
                    .message("Failed to reset password")
                    .statusCode(400)
                    .success(false)
                    .data(null)
                    .build();
        }


    }

    public ResponseDTO sendOTPtoEmail(ForgetPassEmail email) {
        try {

            var user = userRepository.findByEmail(email.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            // Create random OTP
            int otp = otpGenerator();

            // Create HTML-formatted email body
            MailBody mailBody = MailBody.builder()
                    .to(user.getEmail())
                    .subject("Password Reset Request - OTP")
                    .body(
                            "<p>Dear " + user.getFirstName() + ",</p>" +
                                    "<p>We received a request to reset your password for your MedCare account. " +
                                    "Please use the <strong>One-Time Password (OTP)</strong> below to proceed with resetting your password. " +
                                    "This OTP is valid for <strong>10 minutes</strong>.</p>" +
                                    "<p style='font-size: 18px; font-weight: bold; color: #2b8cbe;'>Your OTP: " + otp + "</p>" +
                                    "<p>If you did not request a password reset, please ignore this email or contact our support team for assistance.</p>" +
                                    "<p>Best regards,<br>The MedCare Team</p>"
                    )
                    .build();
            Optional<ForgotPassword> existingForgotPassword = forgotPasswordRepository.findByUser(user);

            if (existingForgotPassword.isPresent()) {
                // If a forgot password entry exists, delete it to prevent multiple OTPs being generated
                forgotPasswordRepository.delete(existingForgotPassword.get());
            }

// Create a new ForgotPassword object with a unique OTP and an expiration time of 10 minutes
            ForgotPassword fp = ForgotPassword.builder()
                    .otp(otp)  // OTP to be sent to the user
                    .expirationTime(new Date(System.currentTimeMillis() + 600_000))  // Expiration time (10 minutes from now)
                    .user(user)  // Link the ForgotPassword object to the user
                    .build();

            // Use the updated HTML email sending method
            emailService.sendHtmlMessage(mailBody);

            forgotPasswordRepository.save(fp);

            return ResponseDTO.builder()
                    .message("Password OTP sent to your email")
                    .statusCode(200)
                    .success(true)
                    .data(null)
                    .build();

        } catch (Exception e) {
            return ResponseDTO.builder()
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

    public ResponseDTO validateOTP(ValidateOTPDto otpValidationRequest) {
        try {
            var user = userRepository.findByEmail(otpValidationRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Integer otp = Integer.parseInt(otpValidationRequest.getOtp());
            ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
                    .orElseThrow(() -> new RuntimeException("OTP not found"));

            if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
                forgotPasswordRepository.delete(fp);
                return ResponseDTO.builder()
                        .message("OTP has expired")
                        .statusCode(400)
                        .success(false)
                        .data(null)
                        .build();
            }


            forgotPasswordRepository.delete(fp);
            return ResponseDTO.builder()
                    .message("Pin is valid")
                    .statusCode(200)
                    .success(true)
                    .data(null)
                    .build();
        } catch (Exception e) {
            if (e.getMessage().equals("OTP not found")) {
                return ResponseDTO.builder()
                        .message("Invalid OTP")
                        .statusCode(400)
                        .success(false)
                        .data(null)
                        .build();
            }
            else {
                return ResponseDTO.builder()
                        .message("Failed to validate OTP")
                        .statusCode(400)
                        .success(false)
                        .data(null)
                        .build();
            }
        }

    }


    public ResponseDTO changePassword(ChangePasswordDto newUser){
        try {
            // Find the user by username
            var user = userRepository.findByEmail(newUser.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Update the user's password
            user.setPassword(passwordEncoder.encode(newUser.getNewPassword()));
            userRepository.save(user);
            return ResponseDTO.builder()
                    .message("Password changed successfully")
                    .statusCode(200)
                    .success(true)
                    .data(null)
                    .build();
        } catch (Exception e) {
            return ResponseDTO.builder()
                    .message("Failed to change password")
                    .statusCode(400)
                    .success(false)
                    .data(null)
                    .build();
        }
    }
}
