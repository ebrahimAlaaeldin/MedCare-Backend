package com.example.medcare.Services;

import com.example.medcare.controller.RegistrationController;
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
import org.mockito.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SignUpServiceTest {


    private MockMvc mockMvc;

    @Mock
    private SignUpService signUpService;

    @Mock
    private AuthenticateService authenticateService;

    @InjectMocks
    private RegistrationController registrationController;

    private PatientDTO patientDTO;
    private DoctorDTO doctorDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();

        // Set up mock objects
        patientDTO = PatientDTO.builder()
                .username("john_doe")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .insuranceNumber("INS123456")
                .emergencyContactNumber("9876543210")
                .build();

        doctorDTO = DoctorDTO.builder()
                .username("dr_jane")
                .firstName("Jane")
                .lastName("Smith")
                .password("password123")
                .email("dr.jane@example.com")
                .phoneNumber("9876543210")
                .licenseNumber("LIC123456")
                .Specialty("Cardiology")
                .build();
    }

    // Test successful doctor registration
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

    // Test failed doctor registration (missing fields)
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
