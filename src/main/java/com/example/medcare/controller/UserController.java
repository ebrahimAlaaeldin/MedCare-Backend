package com.example.medcare.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.medcare.entities.User;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
@PostMapping("/loginGoogle")
PostMapping loginGoogle(@RequestBody String token) {
    return null;
}

}