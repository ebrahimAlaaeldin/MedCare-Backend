package com.example.medcare.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.medcare.Enums.Role;
import com.example.medcare.dto.ClinicAdminDTO;
import com.example.medcare.dto.ClinicDTO;
import com.example.medcare.dto.ResponseMessageDto;
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
    private final PasswordEncoder passwordEncoder;
    

   
    public ResponseEntity<ResponseMessageDto> registerClinic(ClinicDTO request) {

        Optional<ClinicAdmin> admin = clinicAdminRepository.findByUsername(request.getClinicAdmin().getUsername());

        if (!admin.isPresent()) {
            return ResponseEntity.badRequest().body(ResponseMessageDto.builder()
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

        return ResponseEntity.ok(ResponseMessageDto.builder()
                .message("Clinic registered successfully")
                .success(true)
                .statusCode(200)
                .build());
    }


   @Transactional
public synchronized ResponseEntity<ResponseMessageDto> registerClinicandAdminRegistration(ClinicDTO request) {
    try {
        // Validate request
        validateRequest(request);
        
        ClinicAdmin admin = ClinicAdmin.builder()
                .username(request.getClinicAdmin().getUsername())
                .password(passwordEncoder.encode(request.getClinicAdmin().getPassword()))
                .email(request.getClinicAdmin().getEmail())
                .phoneNumber(request.getClinicAdmin().getPhoneNumber())
                .firstName(request.getClinicAdmin().getFirstName())
                .lastName(request.getClinicAdmin().getLastName())
                .role(Role.ADMIN)
                .address(request.getClinicAdmin().getAddress())
                .age(signUpService.calculateAge(request.getClinicAdmin().getDateOfBirth()))
                .birthDate(request.getClinicAdmin().getDateOfBirth())
                .createdAt(LocalDate.now())
                .build();

        Clinic clinic = Clinic.builder()
                .name(request.getClinicName())
                .clinicAdmin(admin)
                .address(request.getAddress())
                .permit(request.getPermit())
                .isVerified(false)
                .isActive(false)
                .createdAt(LocalDate.now())
                .build();

        admin.setClinic(clinic);
        clinicRepository.save(clinic);

        return ResponseEntity.ok(ResponseMessageDto.builder()
                .message("Clinic and Admin registered successfully")
                .success(true)
                .statusCode(200)
                .build());

    } catch (Exception e) {
        return ResponseEntity.badRequest().body(
                ResponseMessageDto.builder()
                        .message("Registration failed: " + e.getMessage())
                        .success(false)
                        .statusCode(400)
                        .build());
    }
}

private void validateRequest(ClinicDTO request) {
    if (request.getClinicAdmin() == null 
            || request.getClinicAdmin().getDateOfBirth() == null
            || request.getClinicAdmin().getUsername() == null
            || request.getClinicName() == null) {
        throw new IllegalArgumentException("Missing required fields");
    }
}



}
