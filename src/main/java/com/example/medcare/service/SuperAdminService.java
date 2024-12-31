package com.example.medcare.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.medcare.dto.ClinicAdminDTO;
import com.example.medcare.dto.ClinicDTO;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.entities.Clinic;
import com.example.medcare.entities.Doctor;
import com.example.medcare.repository.ClinicRepository;
import com.example.medcare.repository.DoctorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SuperAdminService {

    private final DoctorRepository doctorRepository;

    private final ClinicRepository clinicRepository;

    // super admin review pending doctor applications
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
    public ResponseEntity<Object> approveDoctorApplication(DoctorDTO doctorDto) {

        Optional<Doctor> doctor = doctorRepository.findByUsername(doctorDto.getUsername());

        if (doctor.isPresent()) {
            doctor.get().setVerified(true);
            doctorRepository.save(doctor.get());

            return ResponseEntity.ok().body(ResponseMessageDto.builder()
                    .message("Doctor application approved")
                    .success(true)
                    .statusCode(200)
                    .build());
        }

        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ResponseMessageDto.builder()
                    .message("Doctor not found")
                    .success(false)
                    .statusCode(404)
                    .build());
    }

    // super admin review pending clinic applications
    public List<ClinicDTO> getPendingClinics() throws NotFoundException {

        List<Clinic> clinics = clinicRepository.findAllByIsVerifiedFalse();
        if (clinics.isEmpty())
            throw new NotFoundException();
            
        return clinics.stream().map(clinic -> {

            ClinicDTO clinicDTO = ClinicDTO.builder()
                    .clinicId(clinic.getClinicId())
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
    public ResponseEntity<Object> approveClinicApplication(int clinicId) {
        Optional<Clinic> clinic = clinicRepository.findById(clinicId);

        if (clinic.isPresent()) {
            clinic.get().setVerified(true);
            clinicRepository.save(clinic.get());

            return ResponseEntity.ok().body(ResponseMessageDto.builder()
                    .message("Clinic application approved")
                    .success(true)
                    .statusCode(200)
                    .build());
        }

        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ResponseMessageDto.builder()
                    .message("Clinic not found")
                    .success(false)
                    .statusCode(404)
                    .build());
    }
}