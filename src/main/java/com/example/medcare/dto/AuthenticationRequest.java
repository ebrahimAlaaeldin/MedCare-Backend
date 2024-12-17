package com.example.medcare.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;

@Data
@AllArgsConstructor
public class AuthenticationRequest {
    private String username;
    private String email;
    private String password;

}
