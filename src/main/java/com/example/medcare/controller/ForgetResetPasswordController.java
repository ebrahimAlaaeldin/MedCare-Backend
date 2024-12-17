package com.example.medcare.controller;

import com.example.medcare.dto.*;
import com.example.medcare.service.ForgetResetPasswordService;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin

public class ForgetResetPasswordController {
    private final ForgetResetPasswordService forgetResetPasswordService;

    @PostMapping("/resetPassword")
    public ResponseMessageDto resetPassword(
            @RequestBody ResetPasswordDto input,
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader) {
        String token = extractTokenFromHeader(authorizationHeader);
        // Pass the token along with the request body to the service
        return forgetResetPasswordService.resetPassword(input, token);
    }


    @PostMapping("/api/authenticate/forgetPassword")
    public ResponseMessageDto forgetPassword(@RequestBody ForgetPassEmail email) {
        return forgetResetPasswordService.sendOTPtoEmail(email);
    }


    //receive Token on Validation
    @PostMapping("/api/authenticate/forgetPassword/validatePin")
    public ResponseMessageDto validatePin(@RequestBody ValidateOTPDto otpValidationRequest) {
        return forgetResetPasswordService.validateOTP(otpValidationRequest);
    }
    @PostMapping("/api/authenticate/changePassword")
    public ResponseMessageDto changePassword(@RequestBody ChangePasswordDto changePass) {
        return forgetResetPasswordService.changePassword(changePass);
    }



    // Utility method to extract the token
    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header format");
        }
        return authorizationHeader.substring(7); // Remove "Bearer " prefix
    }

}
