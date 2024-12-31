package com.example.medcare.controller;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.medcare.dto.ClinicDTO;
import com.example.medcare.dto.DoctorDTO;

import com.example.medcare.service.SuperAdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/SuperAdmin")
@CrossOrigin
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    @GetMapping("/doctorApplications/pending")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<DoctorDTO>> getPendingApplications() {
        List<DoctorDTO> pendingApplications = superAdminService.returnPendingApplications();
        if (pendingApplications.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pendingApplications, HttpStatus.OK);
    }

    @PutMapping("/doctorApplications/approve")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Object> approveDoctorApplication(@RequestBody DoctorDTO doctorDTO) {
        return superAdminService.approveDoctorApplication(doctorDTO);
    }

    @GetMapping("/clinicApplications/pending")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<ClinicDTO>> viewPendingClinicApplications() {
        try {
            List<ClinicDTO> pendingClinics = superAdminService.getPendingClinics();
            return new ResponseEntity<>(pendingClinics, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/clinicApplications/approve/{clinicId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Object> approveClinicApplication(@PathVariable int clinicId) {
        return superAdminService.approveClinicApplication(clinicId);
    }

}


