package com.example.medcare.Service;

import com.example.medcare.Mappers.ClinicMapper;
import com.example.medcare.Mappers.DoctorMapper;
import com.example.medcare.dto.ClinicDTO;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.entities.Clinic;
import com.example.medcare.entities.Doctor;
import com.example.medcare.repository.ClinicRepository;
import org.junit.jupiter.api.BeforeEach;
import  com.example.medcare.service.ClinicService;
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


public class ClinicServiceTest {

    @Mock
    private ClinicRepository clinicRepository;

    @Mock
    private ClinicMapper clinicMapper;

    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private ClinicService clinicService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllClinics_Success() {
        // Arrange
        List<Clinic> clinics = new ArrayList<>();
        clinics.add(new Clinic());
        clinics.add(new Clinic());

        List<ClinicDTO> clinicDTOs = new ArrayList<>();
        clinicDTOs.add(new ClinicDTO());
        clinicDTOs.add(new ClinicDTO());

        when(clinicRepository.findAllByIsVerifiedFalse()).thenReturn(clinics);
        when(clinicMapper.toDTOs(clinics)).thenReturn(clinicDTOs);

        // Act
        ResponseEntity<Object> response = clinicService.getAllClinics();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(clinicDTOs, response.getBody());
        verify(clinicRepository).findAllByIsVerifiedFalse();
        verify(clinicMapper).toDTOs(clinics);
    }

    @Test
    void testGetAllClinics_NoClinicsAvailable() {
        // Arrange
        when(clinicRepository.findAllByIsVerifiedFalse()).thenReturn(null);

        // Act
        ResponseEntity<Object> response = clinicService.getAllClinics();

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        ResponseMessageDto responseMessage = (ResponseMessageDto) response.getBody();
        assertNotNull(responseMessage);
        assertEquals("No clinics Available", responseMessage.getMessage());
        verify(clinicRepository).findAllByIsVerifiedFalse();
    }

    @Test
    void testGetDoctorsInClinic_Success() {
        // Arrange
        Integer clinicId = 1;
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor());
        doctors.add(new Doctor());

        List<DoctorDTO> doctorDTOs = new ArrayList<>();
        doctorDTOs.add(new DoctorDTO());
        doctorDTOs.add(new DoctorDTO());

        when(clinicRepository.findAllDoctorsByClinicId(clinicId)).thenReturn(doctors);
        when(doctorMapper.toDTO(any(Doctor.class))).thenReturn(new DoctorDTO());

        // Act
        ResponseEntity<Object> response = clinicService.getDoctorsInClinic(clinicId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(doctorDTOs.size(), ((List<?>) response.getBody()).size());
        verify(clinicRepository).findAllDoctorsByClinicId(clinicId);
        verify(doctorMapper, times(doctors.size())).toDTO(any(Doctor.class));
    }

    @Test
    void testGetDoctorsInClinic_NoDoctorsAvailable() {
        // Arrange
        Integer clinicId = 1;
        when(clinicRepository.findAllDoctorsByClinicId(clinicId)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = clinicService.getDoctorsInClinic(clinicId);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        ResponseMessageDto responseMessage = (ResponseMessageDto) response.getBody();
        assertNotNull(responseMessage);
        assertEquals("No doctors Available", responseMessage.getMessage());
        verify(clinicRepository).findAllDoctorsByClinicId(clinicId);
    }
}
