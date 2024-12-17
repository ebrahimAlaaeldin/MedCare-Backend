package com.example.medcare.controller;

import com.example.medcare.controller.RegistrationController;
import com.example.medcare.dto.AuthenticationRequest;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.PatientDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.service.AuthenticateService;
import com.example.medcare.service.SignUpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SignUpService signUpService;

    @Mock
    private AuthenticateService authenticateService;

    @InjectMocks
    private RegistrationController registrationController;

    private PatientDTO patientDTO;
    private DoctorDTO doctorDTO;
    private ResponseMessageDto successResponse;

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
                .role(null) // Assuming it's set internally
                .age(30)
                .dateOfBirth(null) // Assuming it's set internally
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
                .issuingDate(null) // Assuming it's set internally
                .build();

        successResponse = ResponseMessageDto.builder()
                .message("Authentication successfull")
                .success(true)
                .statusCode(200)
                .build();
    }

    // Test successful patient registration
    @Test
    void testRegisterPatient_Success() throws Exception {
        // Mock the response of signUpService
        when(signUpService.patientSignUp(any(PatientDTO.class))).thenReturn(successResponse);

        mockMvc.perform(post("/api/authenticate/register/patient")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"username\": \"john_doe\",\n" +
                        "\"firstName\": \"John\",\n" +
                        "\"lastName\": \"Doe\",\n" +
                        "\"password\": \"password123\",\n" +
                        "\"email\": \"john.doe@example.com\",\n" +
                        "\"phoneNumber\": \"1234567890\",\n" +
                        "\"insuranceNumber\": \"INS123456\",\n" +
                        "\"emergencyContactNumber\": \"9876543210\"\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Authentication successfull"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(200));
    }

    // Test failed patient registration (missing fields)
    @Test
    void testRegisterPatient_Failed() throws Exception {
        // Mock the response of signUpService for invalid patient registration
        ResponseMessageDto errorResponse = ResponseMessageDto.builder()
                .message("Invalid signup request")
                .success(false)
                .statusCode(400)
                .build();
        when(signUpService.patientSignUp(any(PatientDTO.class))).thenReturn(errorResponse);

        mockMvc.perform(post("/api/authenticate/register/patient")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(jsonPath("$.message").value("Invalid signup request"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.statusCode").value(400));
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
                .andExpect(jsonPath("$.message").value("Authentication successfull"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(200));
    }

    // Test failed doctor registration (missing fields)
    @Test
    void testRegisterDoctor_Failed() throws Exception {
        // Mock the response of signUpService for invalid doctor registration
        ResponseMessageDto errorResponse = ResponseMessageDto.builder()
                .message("Invalid signup request")
                .success(false)
                .statusCode(400)
                .build();
        when(signUpService.doctorSignUp(any(DoctorDTO.class))).thenReturn(errorResponse);

        mockMvc.perform(post("/api/authenticate/register/doctor")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(jsonPath("$.message").value("Invalid signup request"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.statusCode").value(400));
    }

    // Test successful login
    @Test
    void testLogin_Success() throws Exception {

        // Mock the authenticationService response
        when(authenticateService.authenticate(any(AuthenticationRequest.class)))
                .thenReturn("JWT-TOKEN-HERE");

        mockMvc.perform(post("/api/authenticate/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"username\": \"john_doe\",\n" +
                        "\"password\": \"password123\"\n" +
                        "}"))
                .andExpect(status().isOk());
    }

    // Test failed login (invalid credentials)
    @Test
    void testLogin_Failed() throws Exception {

        // Mock the authenticationService response for failed login
        when(authenticateService.authenticate(any(AuthenticationRequest.class)))
                .thenReturn(ResponseMessageDto.builder()
                        .message("Invalid credentials")
                        .success(false)
                        .statusCode(401)
                        .build());

        mockMvc.perform(post("/api/authenticate/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"username\": \"invalid_user\",\n" +
                        "\"password\": \"wrong_password\"\n" +
                        "}"))
                .andExpect(jsonPath("$.statusCode").value(401))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }
}
