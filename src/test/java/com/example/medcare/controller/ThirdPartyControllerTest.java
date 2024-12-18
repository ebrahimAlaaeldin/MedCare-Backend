package com.example.medcare.controller;

import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.dto.TokenThirdPartyDto;
import com.example.medcare.service.ThirdPartyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class ThirdPartyControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ThirdPartyController thirdPartyController;

    @Mock
    private ThirdPartyService thirdPartyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(thirdPartyController).build();
    }

    @Test
    void thirdPartyLogin_ShouldReturnSuccessResponse() throws Exception {
        // Arrange
        TokenThirdPartyDto tokenDto = new TokenThirdPartyDto();
        tokenDto.setToken("validToken");

        ResponseMessageDto responseMessage = ResponseMessageDto.builder()
                .success(true)
                .message("Login successful")
                .statusCode(200)
                .data("mockedJwtToken")
                .build();

        when(thirdPartyService.thirdPartyLogin(tokenDto))
                .thenReturn(ResponseEntity.ok(responseMessage));

        // Act & Assert using MockMvc
        mockMvc.perform(post("/api/authenticate/thirdParty/login")
                        .contentType("application/json")
                        .content("{\"token\":\"validToken\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":true,\"message\":\"Login successful\",\"statusCode\":200,\"data\":\"mockedJwtToken\"}"));

        // Verify the service was called once
        verify(thirdPartyService, times(1)).thirdPartyLogin(tokenDto);
    }

    @Test
    void thirdPartyLogin_ShouldReturnErrorResponse() throws Exception {
        // Arrange
        TokenThirdPartyDto tokenDto = new TokenThirdPartyDto();
        tokenDto.setToken("invalidToken");

        ResponseMessageDto responseMessage = ResponseMessageDto.builder()
                .success(false)
                .message("Invalid token")
                .statusCode(401)
                .data(null)
                .build();

        when(thirdPartyService.thirdPartyLogin(tokenDto))
                .thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMessage));

        // Act & Assert using MockMvc
        mockMvc.perform(post("/api/authenticate/thirdParty/login")
                        .contentType("application/json")
                        .content("{\"token\":\"invalidToken\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("{\"success\":false,\"message\":\"Invalid token\",\"statusCode\":401,\"data\":null}"));

        // Verify the service was called once
        verify(thirdPartyService, times(1)).thirdPartyLogin(tokenDto);
    }
}
