package com.example.medcare.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import com.example.medcare.service.ThirdPartyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/authenticate/thirdparty")
public class ThirdPartyController {
    private final ThirdPartyService thirdPartyService;
    @PostMapping("/login")
    public String thirdPartyLogin(@RequestBody String Token) {
        System.out.println(Token);
        return thirdPartyService.ThirdPartyLogin(Token);
    }
}