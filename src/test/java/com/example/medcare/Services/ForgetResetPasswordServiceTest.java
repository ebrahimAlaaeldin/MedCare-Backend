package com.example.medcare.Services;

import com.example.medcare.config.JwtService;
import com.example.medcare.dto.*;
import com.example.medcare.entities.ForgotPassword;
import com.example.medcare.entities.User;
import com.example.medcare.repository.ForgotPasswordRepository;
import com.example.medcare.repository.UserRepository;
import com.example.medcare.service.EmailService;
import com.example.medcare.service.ForgetResetPasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ForgetResetPasswordServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ForgotPasswordRepository forgotPasswordRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ForgetResetPasswordService forgetResetPasswordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendOTPtoEmail_UserExists() {
        // Arrange
        User user = User.builder()
                .email("test@example.com")
                .firstName("John")
                .build();
        ForgetPassEmail email = ForgetPassEmail.builder()
                .email("test@example.com")
                .build();

        when(userRepository.findByEmail(email.getEmail())).thenReturn(Optional.of(user));
        when(forgotPasswordRepository.existsByUser(user)).thenReturn(false);

        // Act
        ResponseEntity<Object> response = forgetResetPasswordService.sendOTPtoEmail(email);
        ResponseMessageDto responseDto = (ResponseMessageDto) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseDto);
        assertTrue(responseDto.isSuccess());
        verify(emailService, times(1)).sendHtmlMessage(any());
        verify(forgotPasswordRepository, times(1)).save(any(ForgotPassword.class));
    }

    @Test
    void testSendOTPtoEmail_UserNotFound() {
        // Arrange
        ForgetPassEmail email = ForgetPassEmail.builder()
                .email("nonexistent@example.com")
                .build();

        when(userRepository.findByEmail(email.getEmail())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> response = forgetResetPasswordService.sendOTPtoEmail(email);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseMessageDto responseDto = (ResponseMessageDto) response.getBody();
        assertNotNull(responseDto);
        assertFalse(responseDto.isSuccess());
        assertEquals("User not found", responseDto.getMessage());
        verify(emailService, times(0)).sendHtmlMessage(any());
        verify(forgotPasswordRepository, times(0)).save(any());
    }

    @Test
    void testValidateOTP_Success() {
        // Arrange
        User user = User.builder().email("test@example.com").build();
        ForgotPassword forgotPassword = ForgotPassword.builder()
                .otp(123456)
                .user(user)
                .expirationTime(new java.util.Date(System.currentTimeMillis() + 1000000000))//
                .build();
        ValidateOTPDto otpDto = ValidateOTPDto.builder()
                .email("test@example.com")
                .otp("123456")
                .build();

        when(userRepository.findByEmail(otpDto.getEmail())).thenReturn(Optional.of(user));
        when(forgotPasswordRepository.findByOtpAndUser(123456, user)).thenReturn(Optional.of(forgotPassword));
        when(jwtService.generateToken(any(), eq(user))).thenReturn("mockToken");

        // Act
        ResponseEntity<Object> response = forgetResetPasswordService.validateOTP(otpDto);
        ResponseMessageDto responseDto = (ResponseMessageDto) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseDto);
        assertTrue(responseDto.isSuccess());
        assertEquals(null, responseDto.getData());
        verify(forgotPasswordRepository, times(1)).delete(forgotPassword);
    }

    @Test
    void testValidateOTP_Expired() {
        // Arrange
        User user = User.builder().email("test@example.com").build();
        ForgotPassword forgotPassword = ForgotPassword.builder()
                .otp(123456)
                .user(user)
                .expirationTime(new java.util.Date(System.currentTimeMillis() - 10000))
                .build();
        ValidateOTPDto otpDto = ValidateOTPDto.builder()
                .email("test@example.com")
                .otp("123456")
                .build();

        when(userRepository.findByEmail(otpDto.getEmail())).thenReturn(Optional.of(user));
        when(forgotPasswordRepository.findByOtpAndUser(123456, user)).thenReturn(Optional.of(forgotPassword));

        // Act
        ResponseEntity<Object> response = forgetResetPasswordService.validateOTP(otpDto);
        ResponseMessageDto responseDto = (ResponseMessageDto) response.getBody();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(responseDto);
        assertFalse(responseDto.isSuccess());
        assertEquals("OTP has expired", responseDto.getMessage());
        verify(forgotPasswordRepository, times(1)).delete(forgotPassword);
    }

    @Test
    void testResetPassword_InvalidOldPassword() {
        // Arrange
        ResetPasswordDto resetPasswordDto = ResetPasswordDto.builder()
                .newPassword("newPassword")
                .oldPassword("wrongPassword")
                .build();
        User user = User.builder()
                .username("testUser")
                .password("encodedOldPassword")
                .build();

        String token = "validToken";
        when(jwtService.extractUsername(token)).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(resetPasswordDto.getOldPassword(), user.getPassword())).thenReturn(false);

        // Act
        ResponseEntity<Object> response = forgetResetPasswordService.resetPassword(resetPasswordDto, token);
        ResponseMessageDto responseDto = (ResponseMessageDto) response.getBody();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(responseDto);
        assertFalse(responseDto.isSuccess());
        assertEquals("Invalid old password", responseDto.getMessage());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void testResetPassword_Success() {
        // Arrange
        ResetPasswordDto resetPasswordDto = ResetPasswordDto.builder()
                .newPassword("newPassword")
                .oldPassword("correctOldPassword")
                .build();
        User user = User.builder()
                .username("testUser")
                .password("encodedOldPassword")
                .build();

        String token = "validToken";
        when(jwtService.extractUsername(token)).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(resetPasswordDto.getOldPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(resetPasswordDto.getNewPassword())).thenReturn("encodedNewPassword");

        // Act
        ResponseEntity<Object> response = forgetResetPasswordService.resetPassword(resetPasswordDto, token);
        ResponseMessageDto responseDto = (ResponseMessageDto) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseDto);
        assertTrue(responseDto.isSuccess());
        assertEquals("Password reset successfully", responseDto.getMessage());
        verify(userRepository, times(1)).save(user);
        assertEquals("encodedNewPassword", user.getPassword());
    }
}
