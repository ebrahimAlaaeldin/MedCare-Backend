package com.example.medcare.service;

import com.example.medcare.dto.loginDTO;
import com.example.medcare.dto.responseDTO;
import com.example.medcare.entities.User;

public interface UserService {
    public responseDTO signup(User user);
    public responseDTO login(loginDTO user);
}
