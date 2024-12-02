package com.example.medcare.controller;

import com.example.medcare.dto.loginDTO;
import com.example.medcare.dto.responseDTO;
import com.example.medcare.entities.User;
import com.example.medcare.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public responseDTO signup(@RequestBody User user){
        return userService.signup(user);
    }

    @PostMapping("/login")
    public responseDTO login(@RequestBody loginDTO user){
        return userService.login(user);
    }



}
