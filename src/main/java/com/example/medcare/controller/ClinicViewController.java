package com.example.medcare.controller;


import com.example.medcare.service.ClinicAdminService;
import com.example.medcare.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clinic")
public class ClinicViewController {

    private final ClinicService clinicService;


    // get all clinics
    @GetMapping("/view")
    public ResponseEntity<Object> getAllClinics() {
        return clinicService.getAllClinics();
    }


    @GetMapping("/doctors/{clinicId}")
    public ResponseEntity<Object> getDoctorsInClinic(@PathVariable Integer clinicId) {
        return clinicService.getDoctorsInClinic(clinicId);
    }
}
