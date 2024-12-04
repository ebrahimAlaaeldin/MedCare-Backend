package com.example.medcare.Services;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.print.Doc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.embedded.License;
import com.example.medcare.entities.Clinic;
import com.example.medcare.entities.Doctor;
import com.example.medcare.repository.DoctorRepository;
import com.example.medcare.service.SuperAdminService;


public class SuperAdminServiceTest {


    @InjectMocks
    private SuperAdminService superAdminServiceUnderTest;

    @Mock
    private DoctorRepository doctorRepository;


    @BeforeEach
    public void setUp() {
        //superAdminServiceUnderTest = new SuperAdminService(doctorRepository);
            MockitoAnnotations.openMocks(this);   
    }
    
    @Test
    public void testGetPendingApplications() {
        // Setup
        Doctor doctor1 = Doctor.builder().username("username").
                firstName("firstName").lastName("lastName")
                .email("email").phoneNumber("phoneNumber")
                .
                license(License.builder().Specialty("specialty").issuingDate(LocalDate.now())
                        .licenseNumber("licenseNumber").build())
                .clinics(List.of(Clinic.builder().name("name").build())).build();

        Doctor doctor2 = Doctor.builder().username("username").
                firstName("firstName").lastName("lastName")
                .email("email").phoneNumber("phoneNumber")
                .
                license(License.builder().Specialty("specialty").issuingDate(LocalDate.now())
                        .licenseNumber("licenseNumber").build())
                .clinics(List.of(Clinic.builder().name("name").build())).build();

        doctorRepository.save(doctor1);
        

        Optional<Doctor> drs = Optional.of(doctor1);
                
        when(doctorRepository.findAllByIsVerified(false)).thenReturn(drs);

        

        // Run the test

        List<DoctorDTO> result = superAdminServiceUnderTest.returnPendingApplications();

        // Verify the results

    }
    


    @Test
    public void testApproveDoctorApplication() {
        // Setup

        // Run the test

        // Verify the results
    }


    @Test
    public void testShouldHandleNullsInPendingApplications() {
        // Setup

        // Run the test

        // Verify the results
    }

    
}
