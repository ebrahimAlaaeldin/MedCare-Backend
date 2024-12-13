package com.example.medcare.Services;
import com.example.medcare.config.JwtService;
import com.example.medcare.dto.*;
import com.example.medcare.entities.ForgotPassword;
import com.example.medcare.entities.User;
import com.example.medcare.repository.ForgotPasswordRepository;
import com.example.medcare.repository.UserRepository;
import com.example.medcare.service.ForgetResetPasswordService;
import com.example.medcare.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ForgetResetPasswordServiceTest {
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private ForgetResetPasswordService forgetResetPasswordService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ForgotPasswordRepository forgotPasswordRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendOTPtoEmail_UserExists() {
        // Setup
        User user = User.builder()
                .email("test@example.com")
                .firstName("John")
                .build();
        ForgetPassEmail email = ForgetPassEmail.builder()
                .email("test@example.com")
                .build();

        when(userRepository.findByEmail(email.getEmail())).thenReturn(Optional.of(user));

        // Run the test
        ResponseMessageDto response = forgetResetPasswordService.sendOTPtoEmail(email);

        // Verify the results
        assertEquals(200, response.getStatusCode());
        assertTrue(response.isSuccess());
        verify(emailService, times(1)).sendHtmlMessage(any());
        verify(forgotPasswordRepository, times(1)).save(any(ForgotPassword.class));
    }

    @Test
    public void testSendOTPtoEmail_UserNotFound() {
        // Setup
        ForgetPassEmail email = ForgetPassEmail.builder()
                .email("nonexistent@example.com")
                .build();

        when(userRepository.findByEmail(email.getEmail())).thenReturn(Optional.empty());

        // Run the test
        ResponseMessageDto response = forgetResetPasswordService.sendOTPtoEmail(email);

        // Verify the results
        assertEquals(400, response.getStatusCode());
        assertFalse(response.isSuccess());
        verify(emailService, times(0)).sendHtmlMessage(any());
        verify(forgotPasswordRepository, times(0)).save(any());
    }

    @Test
    public void testValidateOTP_Success() {
        // Setup
        User user = User.builder()
                .email("test@example.com")
                .build();
        ForgotPassword forgotPassword = ForgotPassword.builder()
                .otp(123456)
                .user(user)
                .expirationTime(new java.util.Date(System.currentTimeMillis() + 1000))
                .build();
        ValidateOTPDto otpValidationRequest = ValidateOTPDto.builder()
                .email("test@example.com")
                .otp(123456)
                .build();

        when(userRepository.findByEmail(otpValidationRequest.getEmail())).thenReturn(Optional.of(user));
        when(forgotPasswordRepository.findByOtpAndUser(otpValidationRequest.getOtp(), user)).thenReturn(Optional.of(forgotPassword));

        // Run the test
        ResponseMessageDto response = forgetResetPasswordService.validateOTP(otpValidationRequest);

        // Verify the results
        assertEquals(200, response.getStatusCode());
        assertTrue(response.isSuccess());
    }

    @Test
    public void testValidateOTP_Expired() {
        // Setup
        User user = User.builder()
                .email("test@example.com")
                .build();
        ForgotPassword forgotPassword = ForgotPassword.builder()
                .otp(123456)
                .user(user)
                .expirationTime(new java.util.Date(System.currentTimeMillis() - 1000))
                .build();
        ValidateOTPDto otpValidationRequest = ValidateOTPDto.builder()
                .email("test@example.com")
                .otp(123456)
                .build();

        when(userRepository.findByEmail(otpValidationRequest.getEmail())).thenReturn(Optional.of(user));
        when(forgotPasswordRepository.findByOtpAndUser(otpValidationRequest.getOtp(), user)).thenReturn(Optional.of(forgotPassword));

        // Run the test
        ResponseMessageDto response = forgetResetPasswordService.validateOTP(otpValidationRequest);

        // Verify the results
        assertEquals(400, response.getStatusCode());
        assertFalse(response.isSuccess());
    }

    @Test
    public void testResetPassword_Success() {
        // Setup
        String token = "validToken";
        ResetPasswordDto input = ResetPasswordDto.builder()
                .newPassword("newPassword")
                .build();
        User user = User.builder()
                .username("testUser")
                .build();

        when(jwtService.extractUsername(token)).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        // Run the test
        ResponseMessageDto response = forgetResetPasswordService.resetPassword(input, token);

        // Verify the results
        assertEquals(200, response.getStatusCode());
        assertTrue(response.isSuccess());
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(userRepository, times(1)).save(user);
    }
}
