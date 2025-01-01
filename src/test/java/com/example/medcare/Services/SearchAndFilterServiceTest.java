package com.example.medcare.Services;

import com.example.medcare.dto.SearchDTO;
import com.example.medcare.entities.Clinic;
import com.example.medcare.entities.Doctor;
import com.example.medcare.repository.ClinicRepository;
import com.example.medcare.repository.DoctorRepository;
import com.example.medcare.service.SearchAndFilterService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SearchAndFilterServiceTest {

    @Mock
    private ClinicRepository clinicRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private SearchAndFilterService searchAndFilterService;

    public SearchAndFilterServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchClinicWithResults() {
        // Arrange
        String name = "Health";
        List<Clinic> mockClinics = Arrays.asList(
                new Clinic(1, "Health Clinic"),
                new Clinic(2, "Healthy Life Clinic")
        );
        when(clinicRepository.findByNameContainingIgnoreCase(name)).thenReturn(mockClinics);

        // Act
        ResponseEntity<Object> response = searchAndFilterService.searchClinic(name);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockClinics, response.getBody());
        verify(clinicRepository, times(1)).findByNameContainingIgnoreCase(name);
    }

    @Test
    void testSearchClinicWithNoResults() {
        // Arrange
        String name = "Unknown";
        when(clinicRepository.findByNameContainingIgnoreCase(name)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<Object> response = searchAndFilterService.searchClinic(name);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.emptyList(), response.getBody());
        verify(clinicRepository, times(1)).findByNameContainingIgnoreCase(name);
    }

    @Test
    void testSearchDoctorWithResults() {
        // Arrange
        SearchDTO searchDTO = new SearchDTO("Cardiology", 1);
        List<Doctor> mockDoctors = Arrays.asList(
                new Doctor(1L, "Dr. John Doe", "Cardiology", new Clinic(1, "Health Clinic")),
                new Doctor(2L, "Dr. Jane Smith", "Cardiology", new Clinic(1, "Health Clinic"))
        );
        when(doctorRepository.findDoctorsBySpecializationAndClinicId(searchDTO.getSpeciality(), searchDTO.getClinicId())).thenReturn(mockDoctors);

        // Act
        ResponseEntity<Object> response = searchAndFilterService.searchDoctor(searchDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockDoctors, response.getBody());
        verify(doctorRepository, times(1)).findDoctorsBySpecializationAndClinicId(searchDTO.getSpeciality(), searchDTO.getClinicId());
    }

    @Test
    void testSearchDoctorWithNoResults() {
        // Arrange
        SearchDTO searchDTO = new SearchDTO("Dermatology", 2);
        when(doctorRepository.findDoctorsBySpecializationAndClinicId(searchDTO.getSpeciality(), searchDTO.getClinicId())).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<Object> response = searchAndFilterService.searchDoctor(searchDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.emptyList(), response.getBody());
        verify(doctorRepository, times(1)).findDoctorsBySpecializationAndClinicId(searchDTO.getSpeciality(), searchDTO.getClinicId());
    }
}
