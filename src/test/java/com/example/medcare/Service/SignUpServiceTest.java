package com.example.medcare.service;

import com.example.medcare.config.JwtService;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.PatientDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.embedded.Address;
import com.example.medcare.entities.Doctor;
import com.example.medcare.entities.Patient;
import com.example.medcare.Enums.Role;
import com.example.medcare.repository.DoctorRepository;
import com.example.medcare.repository.PatientRepository;
import com.example.medcare.service.SignUpService;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class SignUpServiceTest {

    @InjectMocks
    private SignUpService signUpService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    
    private PatientDTO patientDTO;
    private DoctorDTO doctorDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        patientDTO = PatientDTO.builder()
                .username("patient123")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .address(
                    Address.builder()
                        .street("123 Street")
                        .city("City")
                        .country("Country")
                        .build()
                )
                .role(Role.PATIENT)
                .age(30)
                .dateOfBirth(LocalDate.of(1991, 5, 10))
                .insuranceNumber("INS123456")
                .emergencyContactNumber("9876543210")
                .build();

        doctorDTO = DoctorDTO.builder()
                .username("doctor123")
                .firstName("Jane")
                .lastName("Smith")
                .password("password123")
                .email("jane.smith@example.com")
                .phoneNumber("0987654321")
                .age(40)
                .dateOfBirth(LocalDate.of(1981, 7, 20))
                .licenseNumber("LIC123")
                .Specialty("Cardiology")
                .issuingDate(LocalDate.of(2010, 1, 1))
                .build();
    }

    @Test
    void testPatientSignUp_Success() {
        // Mock necessary repository methods
        Mockito.when(patientRepository.save(any(Patient.class))).thenReturn(new Patient());
        Mockito.when(jwtService.generateToken(any(), any(Patient.class))).thenReturn("jwt-token");

        ResponseMessageDto response = signUpService.patientSignUp(patientDTO);

        // Verify the response
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(200, response.getStatusCode());
        assertEquals("Patient registered successfully", response.getMessage());
    }

    @Test
    void testPatientSignUp_InvalidRequest() {
        // Mock invalid patientDTO with missing fields
        patientDTO.setUsername(null);               

        ResponseMessageDto response = signUpService.patientSignUp(patientDTO);

        // Verify the response for invalid signup
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(400, response.getStatusCode());
        assertEquals("Invalid sign up request", response.getMessage());
    }

    @Test
    void testDoctorSignUp_Success() {
        // Mock necessary repository methods
        Mockito.when(doctorRepository.save(any(Doctor.class))).thenReturn(new Doctor());
        Mockito.when(jwtService.generateToken(any(), any(Doctor.class))).thenReturn("jwt-token");

        ResponseMessageDto response = signUpService.doctorSignUp(doctorDTO);

        // Verify the response
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(200, response.getStatusCode());
        assertEquals("Doctor registered successfully, Waiting for License verification", response.getMessage());
    }

    @Test
    void testDoctorSignUp_InvalidRequest() {
        // Mock invalid doctorDTO with missing fields
        doctorDTO.setUsername(null);

        // Call the doctor sign-up method with invalid DTO
        ResponseMessageDto response = signUpService.doctorSignUp(doctorDTO);

        // Verify the response for invalid signup
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(400, response.getStatusCode());
        assertEquals("Invalid sign up request", response.getMessage());
    }
   
}
