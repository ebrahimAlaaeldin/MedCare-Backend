package com.example.medcare.Controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.medcare.controller.ClinicRegistrationController;
import com.example.medcare.dto.ClinicDTO;
import com.example.medcare.dto.ClinicAdminDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.embedded.Address;
import com.example.medcare.embedded.ClinicPermit;
import com.example.medcare.service.ClinicRegistrationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


class ClinicRegistrationControllerTest {

        @Mock
        private ClinicRegistrationService clinicRegistrationService;

        @InjectMocks
        private ClinicRegistrationController Clin;

        private MockMvc mockMvc;

        private ObjectMapper objectMapper;

        private ClinicDTO clinicDTO;
        private ResponseMessageDto successResponse;
        private ResponseMessageDto errorResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(Clin).build();
        objectMapper = new ObjectMapper();

        // Setup test data
        ClinicAdminDTO adminDTO = ClinicAdminDTO.builder()
                .username("admin")
                .password("password")
                .email("admin@clinic.com")
                .firstName("Admin")
                .lastName("Test")
                .dateOfBirth(null)
                .address(new Address("Street", "City", "Country", "12345"))
                .build();

        clinicDTO = ClinicDTO.builder()
                .clinicName("Test Clinic")
                .clinicAdmin(adminDTO)
                .address(new Address("Street", "City", "Country", "12345"))
                .permit(new ClinicPermit(123, null, null))
                .build();

        successResponse = ResponseMessageDto.builder()
                .message("Success")
                .success(true)
                .statusCode(200)
                .build();

        errorResponse = ResponseMessageDto.builder()
                .message("Error")
                .success(false)
                .statusCode(400)
                .build();
    }


    @Test
        public void registerClinic_Success() throws Exception {
            when(clinicRegistrationService.registerClinic(any(ClinicDTO.class)))
                            .thenReturn(ResponseEntity.ok(successResponse));

            mockMvc.perform(post("/api/clinic/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clinicDTO)))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.success").value(true))
                            .andExpect(jsonPath("$.message").value("Success"));
    }


        @Test
        public void registerClinic_Failure() throws Exception {
                when(clinicRegistrationService.registerClinic(any(ClinicDTO.class)))
                                .thenReturn(ResponseEntity.badRequest().body(errorResponse));

                mockMvc.perform(post("/api/clinic/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(clinicDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.success").value(false))
                                .andExpect(jsonPath("$.message").value("Error"));
        }
        
        @Test
        public void registerClinicAdmin_success() throws JsonProcessingException, Exception {

                when(clinicRegistrationService.registerClinicandAdminRegistration(any(ClinicDTO.class)))
                                .thenReturn(ResponseEntity.ok(successResponse));

                mockMvc.perform(post("/api/clinic/register/clinic-admin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(clinicDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.message").value("Success"));

        }

        @Test
        public void registerClinicAdmin_Failure() throws JsonProcessingException, Exception {
                when(clinicRegistrationService.registerClinicandAdminRegistration(any(ClinicDTO.class)))
                                .thenReturn(ResponseEntity.badRequest().body(errorResponse));

                mockMvc.perform(post("/api/clinic/register/clinic-admin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(clinicDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.success").value(false))
                                .andExpect(jsonPath("$.message").value("Error"));
        }
}