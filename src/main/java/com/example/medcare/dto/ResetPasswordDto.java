package com.example.medcare.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetPasswordDto {
    private String oldPassword;
    private String newPassword;


}
