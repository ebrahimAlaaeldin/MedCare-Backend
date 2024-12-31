package com.example.medcare.Controllers;

import com.example.medcare.controller.RegistrationController;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.PatientDTO;
import com.example.medcare.service.AuthenticateService;
import com.example.medcare.service.SignUpService;
import org.junit.jupiter.api.BeforeEach;
import com.example.medcare.dto.ResponseMessageDto;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RegistrationControllerTest {


    private MockMvc mockMvc;

    @Mock
    private SignUpService signUpService;

    @Mock
    private AuthenticateService authenticateService;

    @InjectMocks
    private RegistrationController registrationController;

    private PatientDTO patientDTO;
    private DoctorDTO doctorDTO;
    private ResponseEntity<Object> successResponse;

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

        successResponse = ResponseEntity.ok(
                ResponseMessageDto.builder()
                        .message("Authentication successful")
                        .success(true)
                        .statusCode(200)
                        .build()
        );
    }

    // Test successful doctor registration
    @Test
    void testRegisterDoctor_Success() throws Exception {
        // Mock the response of signUpService
        when(signUpService.doctorSignUp(any(DoctorDTO.class))).thenReturn(successResponse);

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
                .andExpect(jsonPath("$.message").value("Authentication successful"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(200));
    }

    // Test failed doctor registration (missing fields)
    @Test
    void testRegisterDoctor_Failed() throws Exception {
        // Mock the response of signUpService for invalid doctor registration
        ResponseEntity<Object> errorResponse = ResponseEntity.badRequest().body(
                ResponseMessageDto.builder()
                        .message("Invalid signup request")
                        .success(false)
                        .statusCode(400)
                        .build()
        );

        when(signUpService.doctorSignUp(any(DoctorDTO.class))).thenReturn(errorResponse);

        mockMvc.perform(post("/api/authenticate/register/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid signup request"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.statusCode").value(400));
    }
}
