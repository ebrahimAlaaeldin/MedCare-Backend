package com.example.medcare.Controllers;

import com.example.medcare.controller.SuperAdminController;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.service.SuperAdminService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SuperAdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SuperAdminService superAdminService;

    @InjectMocks
    private SuperAdminController superAdminController;

    private DoctorDTO doctorDTO;

    private ResponseEntity<Object> successResponse;
    private ResponseEntity<Object> notFoundResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(superAdminController).build();

        // Sample doctorDTO to use in tests
        doctorDTO = new DoctorDTO();
        doctorDTO.setUsername("doctor1");
        doctorDTO.setFirstName("John");
        doctorDTO.setLastName("Doe");
        doctorDTO.setEmail("john.doe@example.com");
        doctorDTO.setPhoneNumber("1234567890");

        // Sample response messages
        successResponse = ResponseEntity.ok()
        .body(new ResponseMessageDto("Doctor application approved", true, 200, doctorDTO));
        notFoundResponse = ResponseEntity
        .status(404).body(new ResponseMessageDto("Doctor not found", false, 404, doctorDTO));
    }

    @Test
    void testGetPendingApplications_Empty() throws Exception {
        // Mock the service method to return an empty list
        when(superAdminService.returnPendingApplications()).thenReturn(Collections.emptyList());

        // Perform GET request
        mockMvc.perform(get("/api/v1/SuperAdmin/doctorApplications/pending"))
                .andExpect(status().isNoContent()); // HTTP 204 when no pending applications
    }

    @Test
    void testGetPendingApplications_Filled() throws Exception {
        // Mock the service method to return a non-empty list
        when(superAdminService.returnPendingApplications()).thenReturn(Arrays.asList(doctorDTO));

        // Perform GET request
        mockMvc.perform(get("/api/v1/SuperAdmin/doctorApplications/pending"))
                .andExpect(status().isOk()) // HTTP 200 if pending applications exist
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username").value("doctor1"))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

       @Test
    void testApproveDoctorApplication_Success() throws Exception {
        // Configure mock response
        when(superAdminService.approveDoctorApplication(any(DoctorDTO.class))).thenReturn(successResponse);
    
        // Convert DTO to JSON
        String jsonRequest = new ObjectMapper().writeValueAsString(doctorDTO);
    
        // Perform PUT request with JSON body
        mockMvc.perform(put("/api/v1/SuperAdmin/doctorApplications/approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Doctor application approved"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(200));
    
        // Verify service method was called
        verify(superAdminService).approveDoctorApplication(any(DoctorDTO.class));
    }

    
   @Test
    void testApproveDoctorApplication_Failed() throws Exception {
        // Configure mock response
        when(superAdminService.approveDoctorApplication(any(DoctorDTO.class))).thenReturn(notFoundResponse);
    
        // Convert DTO to JSON
        String jsonRequest = new ObjectMapper().writeValueAsString(doctorDTO);
    
        // Perform PUT request with JSON body
        mockMvc.perform(put("/api/v1/SuperAdmin/doctorApplications/approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Doctor not found"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.statusCode").value(404));
    
        // Verify service method was called
        verify(superAdminService).approveDoctorApplication(any(DoctorDTO.class));
    }
}
