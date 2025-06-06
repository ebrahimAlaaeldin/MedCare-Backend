package com.example.medcare.controller;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.dto.TokenThirdPartyDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.medcare.service.ThirdPartyService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/authenticate/thirdParty")
@CrossOrigin

public class ThirdPartyController {
    private final ThirdPartyService thirdPartyService;
    @PostMapping("/login")
    public ResponseEntity<Object> thirdPartyLogin(@RequestBody TokenThirdPartyDto token) throws Exception {
        System.out.println("dfs");
        return thirdPartyService.thirdPartyLogin(token);
    }
}