package com.example.medcare.Controllers;
import com.example.medcare.controller.AppointmentController;
import com.example.medcare.dto.AppointmentDTO;
import com.example.medcare.dto.CancelDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.service.CancelAppointmentService;
import com.example.medcare.service.RescheduleAppointementService;
import com.example.medcare.service.ScheduleAppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AppointmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ScheduleAppointmentService scheduleAppointmentService;

    @Mock
    private CancelAppointmentService cancelAppointmentService;

    @Mock
    private RescheduleAppointementService rescheduleAppointmentService;

    @InjectMocks
    private AppointmentController appointmentController;

    private AppointmentDTO appointmentDTO;
    private CancelDTO cancelDTO;

    private ResponseEntity<Object> successResponse;
    private ResponseEntity<Object> notFoundResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(appointmentController).build();

        // Sample appointment DTO
        appointmentDTO = new AppointmentDTO();
        appointmentDTO.setPatientUsername("testPatient");
        appointmentDTO.setDoctorUsername("testDoctor");
        appointmentDTO.setAppointmentDate("2021-12-31");

        // Sample cancel DTO
        cancelDTO = new CancelDTO(1);
        cancelDTO.setAppointmentId(1);

        // Sample responses
        successResponse = ResponseEntity.ok().body(new ResponseMessageDto("Success", true, 200, null));
        notFoundResponse = ResponseEntity.status(404).body(new ResponseMessageDto("Not Found", false, 404, null));
    }

    @Test
    void testScheduleAppointment_Success() throws Exception {
        when(scheduleAppointmentService.scheduleAppointment(any(AppointmentDTO.class))).thenReturn(successResponse);

        String jsonRequest = new ObjectMapper().writeValueAsString(appointmentDTO);

        mockMvc.perform(post("/api/appointment/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(200));

        verify(scheduleAppointmentService).scheduleAppointment(any(AppointmentDTO.class));
    }

    @Test
    void testCancelAppointment_Success() throws Exception {
        when(cancelAppointmentService.cancelAppointment(any(CancelDTO.class))).thenReturn(successResponse);

        String jsonRequest = new ObjectMapper().writeValueAsString(cancelDTO);

        mockMvc.perform(post("/api/appointment/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(200));

        verify(cancelAppointmentService).cancelAppointment(any(CancelDTO.class));
    }

    @Test
    void testRescheduleAppointment_Success() throws Exception {
        when(rescheduleAppointmentService.rescheduleAppointment(any(AppointmentDTO.class))).thenReturn(successResponse);

        String jsonRequest = new ObjectMapper().writeValueAsString(appointmentDTO);

        mockMvc.perform(post("/api/appointment/reschedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.statusCode").value(200));

        verify(rescheduleAppointmentService).rescheduleAppointment(any(AppointmentDTO.class));
}

}
