package com.example.medcare.controller;

import com.example.medcare.dto.ForgetPasswordDto;
import com.example.medcare.service.ForgetResetPasswordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForgetResetPasswordController {
    private ForgetResetPasswordService forgetResetPasswordService;

    @PostMapping("/forgetPassword")
    public void forgetPassword(@RequestBody ForgetPasswordDto input) {
        forgetResetPasswordService.resetPassword(input);
    }
}
