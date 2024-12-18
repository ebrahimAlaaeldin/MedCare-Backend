package com.example.medcare.Controllers;

import com.example.medcare.controller.SuperAdminController;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.service.SuperAdminService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
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

    private ResponseMessageDto successResponse;
    private ResponseMessageDto notFoundResponse;

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

         successResponse = ResponseMessageDto
                .builder()
                 .message("Doctor application approved")
                    .success(true)
                        .statusCode(200)
                .build();

         notFoundResponse = ResponseMessageDto
                .builder()
                 .message("Doctor not found")
                 .success(false)
                    .statusCode(404)    
                .build();
    }

    @Test
    void testGetPendingApplications_Empty() throws Exception {
        // Mock the service method to return an empty list
        when(superAdminService.returnPendingApplications()).thenReturn(Collections.emptyList());

        // Perform GET request
        mockMvc.perform(get("/api/v1/SuperAdmin/doctorApplications/pending"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetPendingApplications_Filled() throws Exception {
        // Mock the service method to return a list of doctorDTOs
        when(superAdminService.returnPendingApplications()).thenReturn(Collections.singletonList(doctorDTO));

        // Perform GET request
        mockMvc.perform(get("/api/v1/SuperAdmin/doctorApplications/pending"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username").value("doctor1"))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    void testApproveDoctorApplication_Success() throws Exception {
        when(superAdminService.approveDoctorApplication("doctor1")).thenReturn(successResponse);

        mockMvc.perform(put("/api/v1/SuperAdmin/doctorApplications/approve/doctor1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Doctor application approved"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(200));
    }

    
     @Test
    void testApproveDoctorApplication_NotFound() throws Exception {
        when(superAdminService.approveDoctorApplication("unknownDoctor")).thenReturn(notFoundResponse);

        mockMvc.perform(put("/api/v1/SuperAdmin/doctorApplications/approve/unknownDoctor")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Doctor not found"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.statusCode").value(404));
    }
}
