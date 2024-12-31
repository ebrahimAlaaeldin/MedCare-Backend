package com.example.medcare.Services;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AuthenticateServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticateService authenticateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateWithUsernameSuccess() {
        // Arrange
        var request = new AuthenticationRequest("validUser", null, "validPassword");
        var user = createUser();

        when(userRepository.findByUsername("validUser")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(anyMap(), eq(user))).thenReturn("mockToken");

        // Act
        ResponseEntity<Object> response = authenticateService.authenticate(request);
        ResponseMessageDto dto = (ResponseMessageDto) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(dto);
        assertEquals(200, dto.getStatusCode());
        assertTrue(dto.isSuccess());
        assertEquals("mockToken", dto.getData());
        assertEquals("User authenticated successfully", dto.getMessage());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(anyMap(), eq(user));
    }

    @Test
    void testAuthenticateWithEmailSuccess() {
        // Arrange
        var request = new AuthenticationRequest(null, "john.doe@example.com", "validPassword");
        var user = createUser();

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(anyMap(), eq(user))).thenReturn("mockToken");

        // Act
        ResponseEntity<Object> response = authenticateService.authenticate(request);
        ResponseMessageDto dto = (ResponseMessageDto) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(dto);
        assertEquals(200, dto.getStatusCode());
        assertTrue(dto.isSuccess());
        assertEquals("mockToken", dto.getData());
        assertEquals("User authenticated successfully", dto.getMessage());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(anyMap(), eq(user));
    }

    @Test
    void testAuthenticateInvalidCredentials() {
        // Arrange
        var request = new AuthenticationRequest("invalidUser", null, "wrongPassword");
        var user = createUser(); // Create a mock user

        when(userRepository.findByUsername("invalidUser")).thenReturn(Optional.of(user)); // Simulate user existence
        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act
        ResponseEntity<Object> response = authenticateService.authenticate(request);
        ResponseMessageDto dto = (ResponseMessageDto) response.getBody();

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(dto);
        assertEquals(400, dto.getStatusCode());
        assertFalse(dto.isSuccess());
        assertEquals("Invalid credentials", dto.getMessage());
        assertNull(dto.getData());

        verify(userRepository).findByUsername("invalidUser");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtService);
    }

    @Test
    void testAuthenticateUserNotFoundByUsername() {
        // Arrange
        var request = new AuthenticationRequest("nonExistentUser", null, "password");
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> response = authenticateService.authenticate(request);
        ResponseMessageDto dto = (ResponseMessageDto) response.getBody();

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(dto);
        assertEquals(400, dto.getStatusCode());
        assertFalse(dto.isSuccess());
        assertEquals("User not found", dto.getMessage());

        verify(userRepository).findByUsername("nonExistentUser");
        verifyNoInteractions(authenticationManager, jwtService);
    }

    @Test
    void testAuthenticateInvalidRequest() {
        // Arrange
        var request = new AuthenticationRequest(null, null, "password");

        // Act
        ResponseEntity<Object> response = authenticateService.authenticate(request);
        ResponseMessageDto dto = (ResponseMessageDto) response.getBody();

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(dto);
        assertEquals(400, dto.getStatusCode());
        assertFalse(dto.isSuccess());
        assertEquals("Invalid request", dto.getMessage());

        verifyNoInteractions(userRepository, authenticationManager, jwtService);
    }

    private User createUser() {
        User user = new User();
        user.setUsername("validUser");
        user.setEmail("john.doe@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(Role.PATIENT);
        
        return user;
    }
}
