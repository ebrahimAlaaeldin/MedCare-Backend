package com.example.medcare.controller;

import com.example.medcare.dto.AuthenticationRequest;
import com.example.medcare.dto.ForgetPasswordDto;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.service.ForgetResetPasswordService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ForgetResetPasswordController {
    private final ForgetResetPasswordService forgetResetPasswordService;

    @PostMapping("/resetPassword")
    public ResponseMessageDto resetPassword(
            @RequestBody ForgetPasswordDto input,
            @RequestHeader("Authorization") String authorizationHeader) {

        // Extract the token from the Authorization header (Bearer <token>)
        String token = authorizationHeader.startsWith("Bearer ") ?
                authorizationHeader.substring(7) : authorizationHeader;

        // Pass the token along with the request body to the service
        return(forgetResetPasswordService.resetPassword(input, token));
    }
    @PostMapping("/forgetPassword")
    public ResponseMessageDto forgetPassword(@RequestBody String email) {
        return forgetResetPasswordService.forgetPassword(email);
    }
    @PostMapping("/forgetPassword/validatePin")
    public ResponseMessageDto validatePin(@RequestBody String pin) {
        return forgetResetPasswordService.validatePin(pin);
    }
    @PostMapping("/changePassword")
    public ResponseMessageDto changePassword(@RequestBody AuthenticationRequest user) {
        return forgetResetPasswordService.changePassword(user);
    }
}
