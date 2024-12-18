package com.example.medcare.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.medcare.dto.ClinicAdminDTO;
import com.example.medcare.dto.ClinicDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.embedded.Address;
import com.example.medcare.embedded.ClinicPermit;
import com.example.medcare.entities.Clinic;
import com.example.medcare.entities.ClinicAdmin;
import com.example.medcare.Enums.Role;
import com.example.medcare.repository.ClinicAdminRepository;
import com.example.medcare.repository.ClinicRepository;
import com.example.medcare.service.ClinicRegistrationService;
import com.example.medcare.service.SignUpService;

@ExtendWith(MockitoExtension.class)
class ClinicRegistrationServiceTest {

    @Mock
    private ClinicRepository clinicRepository;

    @Mock
    private ClinicAdminRepository clinicAdminRepository;

    @Mock
    private SignUpService signUpService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClinicRegistrationService clinicRegistrationService;

    private ClinicDTO clinicDTO;
    private ClinicAdmin clinicAdmin;
    private Clinic clinic;
    private Address address;
    private ClinicPermit permit;

    @BeforeEach
    void setUp() {
        address = Address.builder()
                .street("Test Street")
                .city("Test City")
                .country("Test Country")
                .zipCode("12345")
                .build();

        permit = ClinicPermit.builder()
                .permitID(123456)
                .issuingDate(LocalDate.now())
                .build();

        ClinicAdminDTO adminDTO = ClinicAdminDTO.builder()
                .username("admin")
                .password("password")
                .email("admin@clinic.com")
                .firstName("Admin")
                .lastName("Test")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address(address)
                .build();

        clinicDTO = ClinicDTO.builder()
                .clinicName("Test Clinic")
                .clinicAdmin(adminDTO)
                .address(address)
                .permit(permit)
                .build();

        clinicAdmin = ClinicAdmin.builder()
                .username("admin")
                .password("encoded_password")
                .email("admin@clinic.com")
                .role(Role.ADMIN)
                .address(address)
                .build();

        clinic = Clinic.builder()
                .name("Test Clinic")
                .clinicAdmin(clinicAdmin)
                .address(address)
                .permit(permit)
                .isVerified(false)
                .isActive(false)
                .createdAt(LocalDate.now())
                .build();
    }

    @Test
    void registerClinic_Success() {
        when(clinicAdminRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(clinicAdmin));
        when(clinicRepository.save(any(Clinic.class)))
                .thenReturn(clinic);

        ResponseEntity<ResponseMessageDto> response = clinicRegistrationService.registerClinic(clinicDTO);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Clinic registered successfully", response.getBody().getMessage());
        verify(clinicRepository).save(any(Clinic.class));
    }

    @Test
    void registerClinicAndAdmin_Success() {
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(signUpService.calculateAge(any())).thenReturn(30);
        when(clinicRepository.save(any(Clinic.class))).thenReturn(clinic);

        ResponseEntity<ResponseMessageDto> response = clinicRegistrationService
                .registerClinicandAdminRegistration(clinicDTO);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Clinic and Admin registered successfully", response.getBody().getMessage());
        verify(clinicRepository).save(any(Clinic.class));
    }

    @Test
    void registerClinicAndAdmin_ValidationFailure() {
        clinicDTO.setClinicAdmin(null);

        ResponseEntity<ResponseMessageDto> response = clinicRegistrationService
                .registerClinicandAdminRegistration(clinicDTO);

        assertEquals(400, response.getStatusCode().value());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Registration failed"));
    }
}