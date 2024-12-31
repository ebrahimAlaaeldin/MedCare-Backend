package com.example.medcare.controller;

import com.example.medcare.controller.SuperAdminController;
import com.example.medcare.dto.ClinicAdminDTO;
import com.example.medcare.dto.ClinicDTO;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.embedded.Address;
import com.example.medcare.service.SuperAdminService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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

public class SuperAdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SuperAdminService superAdminService;

    @InjectMocks
    private SuperAdminController superAdminController;

    private DoctorDTO doctorDTO;
    private ClinicDTO clinicDTO;

    private ResponseEntity<Object> successResponse;
    private ResponseEntity<Object> notFoundResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(superAdminController).build();

        // Sample doctorDTO to use in tests
        doctorDTO = new DoctorDTO();
        doctorDTO.setUsername("doctor1");
        doctorDTO.setFirstName("John");
        doctorDTO.setLastName("Doe");
        doctorDTO.setEmail("john.doe@example.com");
        doctorDTO.setPhoneNumber("1234567890");

        ClinicAdminDTO clinicAdminDTO = ClinicAdminDTO.builder()
                .username("clinicAdmin1")
                .firstName("Jane")
                .lastName("Smith")
                .password("password123")
                .email("abo@123")
                .phoneNumber("1234567890")
                .build();

        clinicDTO = ClinicDTO.builder()
                .clinicName("Clinic1")
                .clinicAdmin(clinicAdminDTO)
                .address(Address.builder().street("123 Main St").city("Springfield").zipCode("62704").build())
                .build();

        // Sample response messages
        successResponse = ResponseEntity.ok()
                .body(new ResponseMessageDto("Doctor application approved", true, 200, doctorDTO));
        notFoundResponse = ResponseEntity
                .status(404).body(new ResponseMessageDto("Doctor not found", false, 404, doctorDTO));
    }

    @Test
    public void testGetPendingApplications_Empty() throws Exception {
        // Mock the service method to return an empty list
        when(superAdminService.returnPendingApplications()).thenReturn(Collections.emptyList());

        // Perform GET request
        mockMvc.perform(get("/api/v1/SuperAdmin/doctorApplications/pending"))
                .andExpect(status().isNoContent()); // HTTP 204 when no pending applications
    }

    @Test
    public void testGetPendingApplications_Filled() throws Exception {
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
    public void testApproveDoctorApplication_Success() throws Exception {
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
    public void testApproveDoctorApplication_Failed() throws Exception {
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

    @Test
    void testGetPendingClinics_Filled() throws Exception {
        // Mock the service method to return a non-empty list
        when(superAdminService.getPendingClinics()).thenReturn(Arrays.asList(clinicDTO));

        // Perform GET request
        mockMvc.perform(get("/api/v1/SuperAdmin/clinicApplications/pending"))
                .andExpect(status().isOk()) // HTTP 200 if pending applications exist
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].clinicName").value("Clinic1"))
                .andExpect(jsonPath("$[0].clinicAdmin.username").value("clinicAdmin1"))
                .andExpect(jsonPath("$[0].address.street").value("123 Main St"))
                .andExpect(jsonPath("$[0].address.city").value("Springfield"))
                .andExpect(jsonPath("$[0].address.zipCode").value("62704"));
    }

    @Test
    public void testGetPendingClinics_Empty() throws Exception {
        // Mock the service method to return an empty list
        when(superAdminService.getPendingClinics()).thenThrow(new NotFoundException());

        // Perform GET request
        mockMvc.perform(get("/api/v1/SuperAdmin/clinicApplications/pending"))
                .andExpect(status().isNoContent()); // HTTP 204 when no pending applications
    }


    @Test
    public void testApproveClinicApplication_Success() throws Exception {
        // Configure mock response
        when(superAdminService.approveClinicApplication(anyInt())).thenReturn(successResponse);

        // Perform PUT request with clinicId
        mockMvc.perform(put("/api/v1/SuperAdmin/clinicApplications/approve/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Doctor application approved"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(200));

        // Verify service method was called
        verify(superAdminService).approveClinicApplication(1);
    }


    @Test
    public void testApproveClinicApplication_Failed() throws Exception {
        // Configure mock response
        when(superAdminService.approveClinicApplication(anyInt())).thenReturn(notFoundResponse);

        // Perform PUT request with clinicId
        mockMvc.perform(put("/api/v1/SuperAdmin/clinicApplications/approve/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Doctor not found"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.statusCode").value(404));

        // Verify service method was called
        verify(superAdminService).approveClinicApplication(1);
    }



}

