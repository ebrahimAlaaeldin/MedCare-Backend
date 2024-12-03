package com.example.medcare.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.service.SuperAdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/SuperAdmin")
public class SuperAdminController {


    private final SuperAdminService applicationsReviewService;

    // Super Admin review the application of the doctor


    @GetMapping("/doctorApplications/pending")
    public ResponseEntity<List<DoctorDTO>> getPendingApplications() {

        return new ResponseEntity<>(applicationsReviewService.returnPendingApplications(), HttpStatus.OK);
    }


    // approve doctor application
    @PutMapping("/doctorApplications/approve/{username}")
    public ResponseEntity<String> approveDoctorApplication(@PathVariable String username) {
        applicationsReviewService.approveDoctorApplication(username);
        return new ResponseEntity<>("Doctor application approved", HttpStatus.OK);
    }


    // TODO: Super Admin review Clinic applications

    

   






    // Super Admin review Clinic applications



    
}
