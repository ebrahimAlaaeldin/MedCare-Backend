package com.example.medcare.controller;

import com.example.medcare.dto.SearchDTO;
import com.example.medcare.service.SearchAndFilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchAndFilterController {
    private final SearchAndFilterService searchAndFilterService;

    @PostMapping("/clinic")
    public ResponseEntity<Object> searchClinic(@RequestParam String clinicName) {
        return searchAndFilterService.searchClinic(clinicName);
    }
    @PostMapping("/doctor")
    public ResponseEntity<Object> searchDoctor(@RequestBody SearchDTO searchDTO) {
        return searchAndFilterService.searchDoctor(searchDTO);
    }
}
