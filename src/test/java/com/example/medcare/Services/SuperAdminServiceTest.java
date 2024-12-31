package com.example.medcare.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;

import com.example.medcare.dto.ClinicDTO;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.embedded.Address;
import com.example.medcare.embedded.ClinicPermit;
import com.example.medcare.embedded.License;
import com.example.medcare.entities.Clinic;
import com.example.medcare.entities.ClinicAdmin;
import com.example.medcare.entities.Doctor;
import com.example.medcare.repository.ClinicRepository;
import com.example.medcare.repository.DoctorRepository;
import com.example.medcare.service.SuperAdminService;


public class SuperAdminServiceTest {
    @Mock
    private DoctorRepository doctorRepository;

    @Mock  
    private ClinicRepository clinicRepository;

    @InjectMocks
    private SuperAdminService superAdminService;

    private Doctor doctor;
    private DoctorDTO doctorDTO;

    private Clinic clinic;
    private ClinicAdmin admin;


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
                .birthDate(LocalDate.of(1991, 5, 10))
                .isVerified(false)
                .license(new License("LIC123456", "General Medicine", LocalDate.of(2020, 1, 1)))
                .build();

                 doctorDTO = DoctorDTO.builder()
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
                                        .build())
                        .Specialty("General Medicine")
                        .licenseNumber("LIC123456")
                        .issuingDate(LocalDate.of(2020, 1, 1))
                         .build();
                        
            admin = ClinicAdmin.builder()
            .username("admin")
            .firstName("John")
            .lastName("Doe")
            .email("admin@clinic.com")
            .phoneNumber("1234567890")
            .address(new Address("Street", "City", "Country", "12345"))
            .build();

        clinic = Clinic.builder()
            .clinicId(1)
            .name("Test Clinic")
            .clinicAdmin(admin)
            .address(new Address("Street", "City", "Country", "12345"))
            .permit(new ClinicPermit(123,LocalDate.now(), LocalDate.now()))
            .isVerified(false)
            .build();
        
    }

    // Test for returning pending applications with pending doctors
    @Test
    void testReturnPendingApplications_WithPendingDoctors() {

        when(doctorRepository.findAllByIsVerified(false)).thenReturn(List.of(doctor));

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
        when(doctorRepository.findAllByIsVerified(false)).thenReturn(List.of());

        List<DoctorDTO> result = superAdminService.returnPendingApplications();

        // Verify the result
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test for approving a doctor application with an existing doctor
    @Test
    void testApproveDoctorApplication_Success() {
    
        // Mock repository behavior
        when(doctorRepository.findByUsername(doctorDTO.getUsername()))
            .thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any(Doctor.class)))
            .thenReturn(doctor);
    
        // Execute test
        ResponseEntity<Object> response = 
            superAdminService.approveDoctorApplication(doctorDTO);
    
        // Verify
        assertTrue(doctor.isVerified());
        verify(doctorRepository).findByUsername(doctorDTO.getUsername());
        verify(doctorRepository).save(doctor);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Doctor application approved", ((ResponseMessageDto) response.getBody()).getMessage());
    }

    // Test for approving a doctor application with a non-existing doctor
    @Test
    void testApproveDoctorApplication_DoctorNotFound() {
        // Mock the repository call to return an empty optional
        when(doctorRepository.findByUsername(" ")).thenReturn(Optional.empty());

        // Call the method and verify that it throws a RuntimeException
        ResponseEntity<Object> response = superAdminService.approveDoctorApplication(doctorDTO);

        // Verify the result
        assertEquals(404, response.getStatusCode().value());
        assertEquals("Doctor not found", ((ResponseMessageDto) response.getBody()).getMessage());

    }

    
     @Test
    void getPendingClinics_Success() throws NotFoundException {
        when(clinicRepository.findAllByIsVerifiedFalse())
            .thenReturn(Arrays.asList(clinic));

        List<ClinicDTO> result = superAdminService.getPendingClinics();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(clinic.getName(), result.get(0).getClinicName());
        assertEquals(clinic.getClinicAdmin().getUsername(), 
            result.get(0).getClinicAdmin().getUsername());
    }

   @Test
   void getPendingClinics_EmptyList() {
       when(clinicRepository.findAllByIsVerifiedFalse())
               .thenReturn(List.of());

       assertThrows(NotFoundException.class,
               () -> superAdminService.getPendingClinics());
   }
    
   @Test
   void approveClinicApplication_Success() {
       when(clinicRepository.findById(1))
               .thenReturn(Optional.of(clinic));
       when(clinicRepository.save(any(Clinic.class)))
               .thenReturn(clinic);

       ResponseEntity<Object> response = superAdminService.approveClinicApplication(1);

       assertEquals(200, response.getStatusCode().value());
       assertTrue(((ResponseMessageDto) response.getBody()).isSuccess());
       assertTrue(clinic.isVerified());
   }

   @Test
    void approveClinicApplication_NotFound() {
        when(clinicRepository.findById(1))
            .thenReturn(Optional.empty());

        ResponseEntity<Object> response = 
            superAdminService.approveClinicApplication(1);

        assertEquals(404, response.getStatusCode().value());
        assertFalse(((ResponseMessageDto)response.getBody()).isSuccess());
    }

   


}

