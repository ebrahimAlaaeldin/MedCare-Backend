package com.example.medcare.controller;

import com.example.medcare.dto.*;
import com.example.medcare.repository.UserRepository;
import com.example.medcare.service.ForgetResetPasswordService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ForgetResetPasswordController {
    private final ForgetResetPasswordService forgetResetPasswordService;
    private final UserRepository userRepository;

    @PostMapping("/resetPassword")
    public ResponseMessageDto resetPassword(
            @RequestBody ResetPasswordDto input,
            @RequestHeader("Authorization") String authorizationHeader) {

        // Extract the token from the Authorization header (Bearer <token>)

        String token = authorizationHeader.startsWith("Bearer ") ?
                authorizationHeader.substring(7) : authorizationHeader;

        // Pass the token along with the request body to the service
        return(forgetResetPasswordService.resetPassword(input, token));
    }




    @PostMapping("/api/authenticate/forgetPassword")
    public ResponseMessageDto forgetPassword(@RequestBody ForgetPassEmail email) {
        return forgetResetPasswordService.sendOTPtoEmail(email);
    }
    @PostMapping("/api/authenticate/forgetPassword/validatePin")
    public ResponseMessageDto validatePin(@RequestBody ValidateOTPDto otpValidationRequest) {
        return forgetResetPasswordService.validateOTP(otpValidationRequest);
    }
    @PostMapping("/api/authenticate/changePassword")
    public ResponseMessageDto changePassword(@RequestBody ChangePasswordDto changePass) {
        return forgetResetPasswordService.changePassword(changePass);
    }
}
