package com.example.medcare.service;

import com.example.medcare.dto.AddDoctorDTO;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.entities.Clinic;
import com.example.medcare.entities.Doctor;
import com.example.medcare.repository.ClinicRepository;
import com.example.medcare.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClinicAdminServiceTest {

    @InjectMocks
    private ClinicAdminService clinicAdminService;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ClinicRepository clinicRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


    }

    @Test
    void testAddDoctorToClinic_Success() {
        // Arrange
        AddDoctorDTO addDoctorDTO = new AddDoctorDTO("testDoctor", 1);
        Doctor doctor = new Doctor();
        doctor.setClinics(new ArrayList<>()); // Initialize the clinics list
        Clinic clinic = new Clinic();

        when(doctorRepository.findByUsername("testDoctor")).thenReturn(Optional.of(doctor));
        when(clinicRepository.findByClinicAdminId(1)).thenReturn(Optional.of(clinic));

        // Act
        ResponseEntity<Object> response = clinicAdminService.addDoctorToClinic(addDoctorDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(doctor.getClinics().contains(clinic));
        verify(doctorRepository).save(doctor);
    }


    @Test
    void testAddDoctorToClinic_DoctorNotFound() {
        AddDoctorDTO addDoctorDTO = new AddDoctorDTO("testDoctor", 1);

        when(doctorRepository.findByUsername("testDoctor")).thenReturn(Optional.empty());

        ResponseEntity<Object> response = clinicAdminService.addDoctorToClinic(addDoctorDTO);

        assertEquals(404, response.getStatusCodeValue());
        verify(doctorRepository, never()).save(any());
    }

    @Test
    void testAddDoctorToClinic_ClinicNotFound() {
        AddDoctorDTO addDoctorDTO = new AddDoctorDTO("testDoctor", 1);
        Doctor doctor = new Doctor();

        when(doctorRepository.findByUsername("testDoctor")).thenReturn(Optional.of(doctor));
        when(clinicRepository.findByClinicAdminId(1)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = clinicAdminService.addDoctorToClinic(addDoctorDTO);

        assertEquals(404, response.getStatusCodeValue());
        verify(doctorRepository, never()).save(any());
    }

    @Test
    void testGetAllDoctorsInClinic_Success() {
        Integer adminId = 1;
        Clinic clinic = new Clinic();
        clinic.setClinicId(1);

        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setEmail("john.doe@example.com");
        doctor.setPhoneNumber("123456789");

        when(clinicRepository.findByClinicAdminId(adminId)).thenReturn(Optional.of(clinic));
        when(clinicRepository.findAllDoctorsByClinicId(1)).thenReturn(List.of(doctor));

        ResponseEntity<Object> response = clinicAdminService.getAllDoctorsInClinic(adminId);

        assertEquals(200, response.getStatusCodeValue());
        List<DoctorDTO> doctorDTOs = (List<DoctorDTO>) response.getBody();
        assertNotNull(doctorDTOs);
        assertEquals(1, doctorDTOs.size());
        assertEquals("John", doctorDTOs.get(0).getFirstName());
    }

    @Test
    void testGetAllDoctorsInClinic_ClinicNotFound() {
        Integer adminId = 1;

        when(clinicRepository.findByClinicAdminId(adminId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = clinicAdminService.getAllDoctorsInClinic(adminId);

        assertEquals(404, response.getStatusCodeValue());
    }
}