package com.example.medcare.dto;


import lombok.Data;

@Data
public class ChangePasswordDto {
    private String email;
    private String newPassword;
}
