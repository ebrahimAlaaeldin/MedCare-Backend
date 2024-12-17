package com.example.medcare.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.medcare.dto.ResponseMessageDto;
import org.springframework.stereotype.Service;

import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.entities.Doctor;
import com.example.medcare.repository.DoctorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SuperAdminService {


    private final DoctorRepository doctorRepository;


    // super admin  review pending doctor applications
    public List<DoctorDTO> returnPendingApplications() {

        if (doctorRepository.findAllByIsVerified(false).isEmpty()) 
            return new ArrayList<>();
        
        // Assuming doctorRepository.findAllByIsApproved returns a List<Doctor>
        Optional<Doctor> doctors = doctorRepository.findAllByIsVerified(false);
        return doctors.stream().map(doctor -> {
            DoctorDTO doctorDTO = new DoctorDTO();
            doctorDTO.setUsername(doctor.getUsername());
            doctorDTO.setFirstName(doctor.getFirstName());
            doctorDTO.setLastName(doctor.getLastName());
            doctorDTO.setEmail(doctor.getEmail());
            doctorDTO.setAddress(doctor.getAddress());
            doctorDTO.setAge(doctor.getAge());
            doctorDTO.setDateOfBirth(doctor.getBirthDate());
            doctorDTO.setPhoneNumber(doctor.getPhoneNumber());
            doctorDTO.setSpecialty(doctor.getLicense().getSpecialty());
            doctorDTO.setIssuingDate(doctor.getLicense().getIssuingDate());
            doctorDTO.setLicenseNumber(doctor.getLicense().getLicenseNumber());
            doctorDTO.setLicenseNumber(doctor.getLicense().getLicenseNumber());
            doctorDTO.setIssuingDate(doctor.getLicense().getIssuingDate());
            return doctorDTO;
        }).collect(Collectors.toList());
    }

    // approving doctor application
    public ResponseMessageDto approveDoctorApplication(String username) {
        Optional<Doctor> doctor = doctorRepository.findByUsername(username);
        
        if (doctor.isPresent()) {
            doctor.get().setIsVerified(true);
            doctorRepository.save(doctor.get());

            return ResponseMessageDto.builder()
                    .message("Doctor application approved")
                    .success(true)
                    .statusCode(200)
                    .build();
        }

        else
            return ResponseMessageDto.builder()
                    .message("Doctor not found")
                    .success(false)
                    .statusCode(404)
                    .build();
}   



    
}
