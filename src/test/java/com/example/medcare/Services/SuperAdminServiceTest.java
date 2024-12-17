package com.example.medcare.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.print.Doc;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.embedded.Address;
import com.example.medcare.embedded.License;
import com.example.medcare.entities.Clinic;
import com.example.medcare.entities.Doctor;
import com.example.medcare.repository.DoctorRepository;
import com.example.medcare.service.SuperAdminService;


public class SuperAdminServiceTest {
    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private SuperAdminService superAdminService;

    private Doctor doctor;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup Doctor object with a sample data
        doctor = Doctor.builder()
                .username("john_doe")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .address(
                    Address.builder()
                            .street("123 Street")
                            .city("City")
                            .country("Country")
                            .build()
                )
                .age(30)
                .birthDate(LocalDate.of(1991, 5, 10))
                .isVerified(false)
                .license(new License("LIC123456", "General Medicine", LocalDate.of(2020, 1, 1)))
                .build();
        
        
    }

    // Test for returning pending applications with pending doctors
    @Test
    void testReturnPendingApplications_WithPendingDoctors() {

        when(doctorRepository.findAllByIsVerified(false)).thenReturn(Optional.of(doctor));

        List<DoctorDTO> result = superAdminService.returnPendingApplications();

        // Verify the result
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("john_doe", result.get(0).getUsername());
    }

    // Test for returning pending applications with no pending doctors
    @Test
    void testReturnPendingApplications_NoPendingDoctors() {
        // Mock the repository call to return an empty list
        when(doctorRepository.findAllByIsVerified(false)).thenReturn(Optional.empty());

        List<DoctorDTO> result = superAdminService.returnPendingApplications();

        // Verify the result
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test for approving a doctor application with an existing doctor
    @Test
    void testApproveDoctorApplication_Success() {
        // Mock the repository call to return a doctor by username
        when(doctorRepository.findByUsername("john_doe")).thenReturn(Optional.of(doctor));

        // Call the method to approve
        superAdminService.approveDoctorApplication("john_doe");

        // Verify that the doctor's `isVerified` flag was set to true
        assertTrue(doctor.getIsVerified());

        // Verify that save was called on the repository
        verify(doctorRepository, times(1)).save(doctor);
    }

    // Test for approving a doctor application with a non-existing doctor
    @Test
    void testApproveDoctorApplication_DoctorNotFound() {
        // Mock the repository call to return an empty optional
        when(doctorRepository.findByUsername(" ")).thenReturn(Optional.empty());

        // Call the method and verify that it throws a RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            superAdminService.approveDoctorApplication(" ")
        );

        // Verify the exception message
        assertEquals("Doctor not found", exception.getMessage());

    }
}

