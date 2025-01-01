package com.example.medcare.controller;


import com.example.medcare.dto.AddDoctorDTO;
import com.example.medcare.service.ClinicAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ClinicAdminController {

    private final ClinicAdminService clinicAdminService;



    @PostMapping("/add")
    public ResponseEntity<Object> addDoctorToClinic(@RequestBody AddDoctorDTO addDoctorDTO) {
        return clinicAdminService.addDoctorToClinic(addDoctorDTO);
    }

    @GetMapping("/doctors/{adminId}")
    public ResponseEntity<Object> getAllDoctorsInClinic(@PathVariable Integer adminId) {
        return clinicAdminService.getAllDoctorsInClinic(adminId);
    }



}
