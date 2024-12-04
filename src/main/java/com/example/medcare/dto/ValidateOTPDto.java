package com.example.medcare.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ValidateOTPDto {
    private String email;
    private Integer otp;
}
