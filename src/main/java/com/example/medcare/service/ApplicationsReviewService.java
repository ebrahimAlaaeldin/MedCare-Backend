package com.example.medcare.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.entities.Doctor;
import com.example.medcare.repository.DoctorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationsReviewService {


    private final DoctorRepository doctorRepository;


    // super admin  review pending doctor applications
    public List<DoctorDTO> returnPendingApplications() {
        // Assuming doctorRepository.findAllByIsApproved returns a List<Doctor>
        Optional<Doctor> doctors = doctorRepository.findAllByIsVerified(false);
        return doctors.stream().map(doctor -> {
            DoctorDTO doctorDTO = new DoctorDTO();
            doctorDTO.setUsername(doctor.getUsername());
            doctorDTO.setFirstName(doctor.getFirstName());
            doctorDTO.setLastName(doctor.getLastName());
            doctorDTO.setEmail(doctor.getEmail());
            doctorDTO.setPhoneNumber(doctor.getPhoneNumber());
            doctorDTO.setSpecialty(doctor.getLicense().getSpecialty());
            doctorDTO.setIssuingDate(doctor.getLicense().getIssuingDate());
            doctorDTO.setLicenseNumber(doctor.getLicense().getLicenseNumber());
            doctorDTO.setClinicNames(
                    doctor.getClinics().stream().map(clinic -> clinic.getName()).collect(Collectors.toList()));
            doctorDTO.setLicenseNumber(doctor.getLicense().getLicenseNumber());
            doctorDTO.setIssuingDate(doctor.getLicense().getIssuingDate());
            return doctorDTO;
        }).collect(Collectors.toList());
    }


    // approving doctor application
    public void approveDoctorApplication(String username) {
        Optional<Doctor> doctor = doctorRepository.findByUsername(username);
        
        if (doctor.isPresent()) {
            doctor.get().setIsVerified(true);
            doctorRepository.save(doctor.get());
        }
}



    
}
