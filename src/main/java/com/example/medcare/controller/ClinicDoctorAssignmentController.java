package com.example.medcare.controller;


import com.example.medcare.dto.ClinicDoctorAssignmentDTO;
import com.example.medcare.dto.DoctorClinicDTO;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.repository.ClinicRepository;
import com.example.medcare.service.AssignClinicDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clinic/assignment")
@RequiredArgsConstructor
@CrossOrigin
public class ClinicDoctorAssignmentController {

    private final ClinicRepository clinicRepository;
    private final AssignClinicDoctorService assignClinicDoctors;
    @PostMapping("/assign")
    public ResponseEntity<Object> assignDoctorToClinic(@RequestBody ClinicDoctorAssignmentDTO clinicDoctorAssignmentDTO, @AuthenticationPrincipal UserDetails userDetails) {
        String ClinicSuperAdmin = userDetails.getUsername();
        Integer clinicId = clinicRepository.findClinicByClinicAdminUsername(ClinicSuperAdmin);
        clinicDoctorAssignmentDTO.setClinicId(clinicId);
        try {
            return ResponseEntity.ok(assignClinicDoctors.assignDoctorToClinic(clinicDoctorAssignmentDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/get/clinic/assigne")
    public ResponseEntity<Object> getDoctorClinicAssignment(@AuthenticationPrincipal UserDetails userDetails) {
        String ClinicSuperAdmin = userDetails.getUsername();
        Integer clinicId = clinicRepository.findClinicByClinicAdminUsername(ClinicSuperAdmin);
        try {
            return ResponseEntity.ok(assignClinicDoctors.getDoctorClinicAssignment(clinicId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    //contain day and clinic username
    @GetMapping("/get/day/assigne")
    public ResponseEntity<Object> getAssigneByDay(@RequestBody DoctorClinicDTO day) {
        try {
            return ResponseEntity.ok(assignClinicDoctors.getAssigneByDay(day));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




}
