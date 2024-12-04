package com.example.medcare.service;

import org.springframework.stereotype.Service;

import com.example.medcare.entities.User;
import com.example.medcare.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import com.example.medcare.config.*;
@Service
@RequiredArgsConstructor
public class ThirdPartyService {
    private UserRepository userRepository;
    private final JwtService jwtService;
    
    public String ThirdPartyLogin(String Token) {
        String email = jwtService.extractEmail(Token);
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            System.out.println("usernotfound");
            return "User not found";
        } else {
            System.out.println(email);
            return "User found";
        }
    }
}
