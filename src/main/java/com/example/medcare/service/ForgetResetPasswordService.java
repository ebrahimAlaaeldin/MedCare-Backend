package com.example.medcare.service;

import com.example.medcare.config.JwtService;
import com.example.medcare.dto.*;
import com.example.medcare.entities.ForgotPassword;
import com.example.medcare.entities.User;
import com.example.medcare.repository.ForgotPasswordRepository;
import com.example.medcare.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Service
@AllArgsConstructor
public class ForgetResetPasswordService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final EmailService emailService;



    public ResponseEntity<Object> resetPassword(ResetPasswordDto input, String token) {
        // Reset the password
        var username = jwtService.extractUsername(token);
        try {

            // Find the user by username
            var user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if(!passwordEncoder.matches(input.getOldPassword(),user.getPassword())){
                throw new RuntimeException("Invalid old password");
            }
            else {

                // Update the user's password
                user.setPassword(passwordEncoder.encode(input.getNewPassword()));
                userRepository.save(user);

                return ResponseEntity.ok().body(ResponseMessageDto.builder()
                        .message("Password reset successfully")
                        .success(true)
                        .statusCode(200)
                        .data(null)
                        .build());
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseMessageDto.builder()
                    .message(e.getMessage())
                    .success(false)
                    .statusCode(400)
                    .data(null)
                    .build());
        }


    }

    public ResponseEntity<Object> sendOTPtoEmail(ForgetPassEmail email) {
        try {

            var user = userRepository.findByEmail(email.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (forgotPasswordRepository.existsByUser(user)) {
                // If a forgot password entry exists, delete it to prevent multiple OTPs being generated
                forgotPasswordRepository.delete(forgotPasswordRepository.findByUser(user).get());
            }
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

            // Create a new ForgotPassword object with a unique OTP and an expiration time of 10 minutes
            ForgotPassword fp = ForgotPassword.builder()
                    .otp(otp)  // OTP to be sent to the user
                    .expirationTime(new Date(System.currentTimeMillis() + 10 * 60 * 1000))  // 10 minutes from now
                    .user(user)  // Link the ForgotPassword object to the user
                    .build();

            // Use the updated HTML email sending method
            emailService.sendHtmlMessage(mailBody);
            forgotPasswordRepository.save(fp);

            return ResponseEntity.ok().body(ResponseMessageDto.builder()
                    .message("OTP sent successfully to "+ user.getEmail())
                    .success(true)
                    .statusCode(200)
                    .data(null)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseMessageDto.builder()
                    .message(e.getMessage())
                    .success(false)
                    .statusCode(400)
                    .data(null)
                    .build());
        }
    }

    private int otpGenerator() {
        Random random = new Random();
        int randomOtp= random.nextInt(100_000, 999_999);
        //check if the otp is in the database
        if(forgotPasswordRepository.existsByOtp(randomOtp)){
            return otpGenerator();
        }
        else{
            return randomOtp;
        }
    }

    public ResponseEntity<Object> validateOTP(ValidateOTPDto otpValidationRequest) {
        try {
            User user = userRepository.findByEmail(otpValidationRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Integer otp = Integer.parseInt(otpValidationRequest.getOtp());
            ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
                    .orElseThrow(() -> new RuntimeException("Invalid OTP"));

            if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
                forgotPasswordRepository.delete(fp);
                throw new RuntimeException("OTP has expired");
            }
            String jwtToken=jwtService.generateToken(otp,user);
            forgotPasswordRepository.delete(fp);

            return ResponseEntity.ok().body(ResponseMessageDto.builder()
                    .message("OTP validated successfully")
                    .success(true)
                    .statusCode(200)
                    .data(jwtToken)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseMessageDto.builder()
                    .message(e.getMessage())
                    .success(false)
                    .statusCode(400)
                    .data(null)
                    .build());
        }

    }


    public ResponseEntity<Object> changePassword(ChangePasswordDto newUser, String otpToken){
        try {
            // Find the user by username
            var user = userRepository.findByEmail(newUser.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            var extractedOtp = jwtService.extractClaim(otpToken, claims -> claims.get("OTP", Integer.class));
            if (extractedOtp == null) {
                throw new RuntimeException("Invalid Credentials");
            }
            var username = jwtService.extractUsername(otpToken);

            if(!user.getUsername().equals(username)){
                throw new RuntimeException("Invalid user");
            }
            // Update the user's password
            user.setPassword(passwordEncoder.encode(newUser.getNewPassword()));
            userRepository.save(user);
            Map<String, Object> claims = Map.of("role", user.getRole().toString(),
                    "firstName", user.getFirstName(),
                    "lastName", user.getLastName(),
                    "email", user.getEmail(),
                    "username", user.getUsername()
            );

            String jwtToken = jwtService.generateToken(claims,user);

            return ResponseEntity.ok().body(ResponseMessageDto.builder()
                    .message("Password changed successfully")
                    .success(true)
                    .statusCode(200)
                    .data(jwtToken)
                    .build());
        } catch (Exception e) {

            return ResponseEntity.badRequest().body(ResponseMessageDto.builder()
                    .message(e.getMessage())
                    .success(false)
                    .statusCode(400)
                    .data(null)
                    .build());
        }
    }
}
