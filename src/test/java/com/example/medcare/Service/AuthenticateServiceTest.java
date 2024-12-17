package com.example.medcare.service;
import com.example.medcare.Enums.Role;
import com.example.medcare.config.JwtService;
import com.example.medcare.dto.AuthenticationRequest;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.entities.User;
import com.example.medcare.repository.UserRepository;
import com.example.medcare.service.AuthenticateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticateServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock //
    private UserRepository userRepository;


    @InjectMocks
    private AuthenticateService authenticateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateSuccess() {
        // Arrange
        var request = new AuthenticationRequest("validUser", "validPassword");
        var user = new User();
        user.setUsername("validUser");
        user.setRole(Role.PATIENT);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");

        when(userRepository.findByUsername("validUser")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(anyMap(), eq(user))).thenReturn("mockToken");

        // Act
        var response = authenticateService.authenticate(request);

        // Assert
        assertTrue(response instanceof ResponseMessageDto);
        ResponseMessageDto dto = (ResponseMessageDto) response;
        assertEquals(200, dto.getStatusCode());
        assertEquals("Authentication successful", dto.getMessage());
        assertTrue(dto.isSuccess());
        assertEquals("mockToken", dto.getData());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(anyMap(), eq(user));
    }

    @Test
    void testAuthenticateInvalidCredentials() {
        // Arrange
        var request = new AuthenticationRequest("invalidUser", "invalidPassword");

        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act
        var response = authenticateService.authenticate(request);

        // Assert
        assertTrue(response instanceof ResponseMessageDto);
        ResponseMessageDto dto = (ResponseMessageDto) response;
        assertEquals(401, dto.getStatusCode());
        assertEquals("Invalid credentials", dto.getMessage());
        assertFalse(dto.isSuccess());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtService, userRepository);
    }

    @Test
    void testAuthenticateUserNotFound() {
        // Arrange
        var request = new AuthenticationRequest("nonExistentUser", "password");

        when(userRepository.findByUsername("nonExistentUser"))
                .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authenticateService.authenticate(request);
        });

        assertEquals("User not found", exception.getMessage());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByUsername("nonExistentUser");
        verifyNoInteractions(jwtService);
    }

    @Test
    void testAuthenticateJwtGenerationFailure() {
        // Arrange
        var request = new AuthenticationRequest("validUser", "validPassword");
        var user = new User();
        user.setUsername("validUser");
        user.setRole(Role.PATIENT);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");

        when(userRepository.findByUsername("validUser")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(anyMap(), eq(user)))
                .thenThrow(new RuntimeException("JWT generation error"));

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticateService.authenticate(request);
        });

        // Assert
        assertEquals("JWT generation error", exception.getMessage());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(anyMap(), eq(user));
    }
}
