package com.example.medcare.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.medcare.dto.ClinicAdminDTO;
import com.example.medcare.dto.ClinicDTO;
import com.example.medcare.dto.ResponseDTO;
import com.example.medcare.entities.Clinic;
import com.example.medcare.entities.ClinicAdmin;
import com.example.medcare.repository.ClinicAdminRepository;
import com.example.medcare.repository.ClinicRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ClinicRegistrationService {

    private final ClinicRepository clinicRepository;
    private final ClinicAdminRepository clinicAdminRepository;
    private final SignUpService signUpService;

   
    public ResponseEntity<ResponseDTO> registerClinic(ClinicDTO request) {

        Optional<ClinicAdmin> admin = clinicAdminRepository.findByUsername(request.getClinicAdmin().getUsername());

        if (!admin.isPresent()) {
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                    .message("Admin not found")
                    .success(false)
                    .statusCode(400)
                    .build());
        }

        Clinic clinic = Clinic.builder()
                .name(request.getClinicName())
                .clinicAdmin(admin.get())
                .address(request.getAddress())
                .permit(request.getPermit())
                .isVerified(false)
                .isActive(false)
                .createdAt(LocalDate.now())
                .isVerified(false)
                .build();

        clinicRepository.save(clinic);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("Clinic registered successfully")
                .success(true)
                .statusCode(200)
                .build());
    }


    // TODO: make Clinic and Clinic Admin registration
    @Transactional
    public ResponseEntity<ResponseDTO> registerClinicandAdminRegistration(ClinicDTO request) {

        // admin registration
        if (request.getClinicAdmin() == null || request.getClinicAdmin().getUsername().isEmpty() ||
                request.getClinicAdmin().getPassword() == null || request.getClinicAdmin().getPassword().isEmpty() ||
                request.getClinicAdmin().getEmail() == null) {
            return ResponseEntity.badRequest().body(
                    ResponseDTO.builder()
                            .message("Invalid Admin sign up request")
                            .success(false)
                            .statusCode(400)
                            .build());
        }

        ClinicAdminDTO admin = request.getClinicAdmin();

        signUpService.adminSignUp(admin);

        // Clinc registration
        if (request.getPermit() == null || request.getClinicName() == null) {
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                    .message("Invalid clinic registration request")
                    .success(false)
                    .statusCode(400)
                    .build());
        }

        ClinicAdmin clinicAdmin = clinicAdminRepository.findByUsername(admin.getUsername()).get();
        Clinic clinic = Clinic.builder()
                .name(request.getClinicName())
                .clinicAdmin(clinicAdmin)
                .address(request.getAddress())
                .permit(request.getPermit())
                .isVerified(false)
                .isActive(false)
                .createdAt(LocalDate.now())
                .isVerified(false)
                .build();

        

        clinicRepository.save(clinic);

        return ResponseEntity.ok(ResponseDTO.builder()
                .message("Clinic and Admin registered successfully")
                .success(true)
                .statusCode(200)
                .build());

    }

}
