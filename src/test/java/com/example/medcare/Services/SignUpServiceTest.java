package com.example.medcare.Services;

import com.example.medcare.controller.RegistrationController;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.PatientDTO;
import com.example.medcare.service.AuthenticateService;
import com.example.medcare.service.SignUpService;
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
    void testRegisterDoctor_Success() throws Exception {
        // Mock the response of signUpService
        when(signUpService.doctorSignUp(any(DoctorDTO.class))).thenAnswer(invocation -> {
            DoctorDTO dto = invocation.getArgument(0);
            Map<String, String> mockJwtToken = Map.of("token", "JWT-TOKEN-HERE");
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("message", "Doctor registered successfully");
            return ResponseEntity.ok().headers(responseHeaders).body(mockJwtToken);
        });

        mockMvc.perform(post("/api/authenticate/register/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "\"username\": \"dr_jane\",\n" +
                                "\"firstName\": \"Jane\",\n" +
                                "\"lastName\": \"Smith\",\n" +
                                "\"password\": \"password123\",\n" +
                                "\"email\": \"dr.jane@example.com\",\n" +
                                "\"phoneNumber\": \"9876543210\",\n" +
                                "\"licenseNumber\": \"LIC123456\",\n" +
                                "\"specialty\": \"Cardiology\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(header().string("message", "Doctor registered successfully"))
                .andExpect(jsonPath("$.token").value("JWT-TOKEN-HERE"));
    }

    // Test failed doctor registration (missing fields)
    @Test
    void testRegisterDoctor_Failed() throws Exception {
        // Mock the response of signUpService for invalid doctor registration
        when(signUpService.doctorSignUp(any(DoctorDTO.class))).thenAnswer(invocation -> {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("message", "Invalid sign up request");
            return ResponseEntity.badRequest().headers(responseHeaders).build();
        });

        mockMvc.perform(post("/api/authenticate/register/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("message", "Invalid sign up request"));
    }
}
