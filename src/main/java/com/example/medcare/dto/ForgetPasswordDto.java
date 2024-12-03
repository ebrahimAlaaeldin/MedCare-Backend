package com.example.medcare.dto;

import lombok.Data;

@Data
public class ForgetPasswordDto {
    private String oldPassword;
    private String newPassword;
}
