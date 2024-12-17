package com.example.medcare.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.medcare.dto.ClinicAdminDTO;
import com.example.medcare.dto.ClinicDTO;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.entities.Doctor;
import com.example.medcare.repository.ClinicRepository;
import com.example.medcare.repository.DoctorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class SuperAdminService {

    private final DoctorRepository doctorRepository;

    private final ClinicRepository clinicRepository;

    // super admin  review pending doctor applications
    public List<DoctorDTO> returnPendingApplications() {

        List<Doctor> doctors = doctorRepository.findAllByIsVerified(false);

        if (doctors.isEmpty() || doctors == null)
            return new ArrayList<>();

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
    public ResponseDTO approveDoctorApplication(String username) {
        Optional<Doctor> doctor = doctorRepository.findByUsername(username);

        if (doctor.isPresent()) {
            doctor.get().setVerified(true);
            doctorRepository.save(doctor.get());

            return ResponseDTO.builder()
                    .message("Doctor application approved")
                    .success(true)
                    .statusCode(200)
                    .build();
        }

        else
            return ResponseDTO.builder()
                    .message("Doctor not found")
                    .success(false)
                    .statusCode(404)
                    .build();
    }

        // super admin review pending clinic applications
    public List<ClinicDTO> getPendingClinics() throws NotFoundException {

            if (clinicRepository.getPendingClinics().isEmpty())
                throw new NotFoundException(); // throw exception if no pending clinics 

            // Assuming clinicRepository.getPendingClinics() returns a List<Clinic>
            List<Clinic> clinics = clinicRepository.getPendingClinics();
        
            return clinics.stream().map(clinic -> {

                ClinicDTO clinicDTO = ClinicDTO.builder()
                        .clinicName(clinic.getName())
                        .address(clinic.getAddress())
                        .permit(clinic.getPermit())
                        .clinicAdmin(ClinicAdminDTO.builder()
                                .username(clinic.getClinicAdmin().getUsername())
                                .firstName(clinic.getClinicAdmin().getFirstName())
                                .lastName(clinic.getClinicAdmin().getLastName())
                                .email(clinic.getClinicAdmin().getEmail())
                                .phoneNumber(clinic.getClinicAdmin().getPhoneNumber())
                                .address(clinic.getClinicAdmin().getAddress())
                                .build())
                        .build();
                return clinicDTO;
            }).collect(Collectors.toList());

        }

        // approving clinic application
        public ResponseDTO approveClinicApplication(int clinicId) {
            Optional<Clinic> clinic = clinicRepository.findById(clinicId);

            if (clinic.isPresent()) {
                clinic.get().setVerified(true);
                clinicRepository.save(clinic.get());

                return ResponseDTO.builder()
                        .message("Clinic application approved")
                        .success(true)
                        .statusCode(200)
                        .build();
            }

            else
                return ResponseDTO.builder()
                        .message("Clinic not found")
                        .success(false)
                        .statusCode(404)
                        .build();
        }
}