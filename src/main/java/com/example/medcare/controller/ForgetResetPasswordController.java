package com.example.medcare.controller;

import com.example.medcare.dto.ForgetPasswordDto;
import com.example.medcare.dto.ResponseMessageDto;
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
    @PostMapping("/forgetPassword")
    public ResponseMessageDto forgetPassword(
            @RequestBody ForgetPasswordDto input,
            @RequestHeader("Authorization") String authorizationHeader) {

        // Extract the token from the Authorization header (Bearer <token>)
        String token = authorizationHeader.startsWith("Bearer ") ?
                authorizationHeader.substring(7) : authorizationHeader;

        // Pass the token along with the request body to the service
        return(forgetResetPasswordService.resetPassword(input, token));
    }
}
