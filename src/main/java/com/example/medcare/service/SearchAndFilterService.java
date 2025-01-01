package com.example.medcare.service;

import com.example.medcare.dto.SearchDTO;
import com.example.medcare.entities.Clinic;
import com.example.medcare.entities.Doctor;
import com.example.medcare.repository.ClinicRepository;
import com.example.medcare.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchAndFilterService {
    private final ClinicRepository clinicRepository;
    private final DoctorRepository doctorRepository;
    public ResponseEntity<Object> searchClinic(String clinicName) {
        List<Clinic> clinics = clinicRepository.findByNameContainingIgnoreCase(clinicName);
        return ResponseEntity.ok(clinics);
    }

    public ResponseEntity<Object> searchDoctor(SearchDTO searchDTO) {

        List<Doctor> doctors = doctorRepository.findDoctorsBySpecializationAndClinicId(searchDTO.getSpeciality(), searchDTO.getClinicId());
        return ResponseEntity.ok(doctors);
    }
}
