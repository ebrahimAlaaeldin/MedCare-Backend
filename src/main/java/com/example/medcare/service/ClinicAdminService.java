package com.example.medcare.service;

import com.example.medcare.dto.AddDoctorDTO;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.entities.Clinic;
import com.example.medcare.entities.Doctor;
import com.example.medcare.repository.ClinicAdminRepository;
import com.example.medcare.repository.ClinicRepository;
import com.example.medcare.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClinicAdminService {

    private final DoctorRepository doctorRepository;
    private final ClinicRepository clinicRepository;
    private final ClinicAdminRepository clinicAdminRepository;

    // add doctor to clinic
    @Transactional
    public ResponseEntity<Object> addDoctorToClinic(AddDoctorDTO addDoctorDTO) {
        // check if doctor exists
        Optional<Doctor> doctor = doctorRepository.findByUsername(addDoctorDTO.getDoctorUsername());

        if(doctor.isEmpty()) {
            return new ResponseEntity<>(
                    ResponseMessageDto.builder().message("Doctor not found").success(false).statusCode(404).build()
                    , HttpStatus.NOT_FOUND);
        }

        Optional<Clinic> clinic = clinicRepository.findByClinicAdminId(addDoctorDTO.getAdminId());
        if(clinic.isEmpty()) {
            return new ResponseEntity<>(
                    ResponseMessageDto.builder().message("Clinic not found").success(false).statusCode(404).build()
                    , HttpStatus.NOT_FOUND);
        }
        // add doctor to clinic
        doctor.get().getClinics().add(clinic.get());
        doctorRepository.save(doctor.get());

        return new ResponseEntity<>(
                ResponseMessageDto.builder().message("Doctor added to clinic").success(true).statusCode(200).build()
                , HttpStatus.OK);
    }

    // Clinic admin can view all the doctors in his clinic
    @Transactional()
    public ResponseEntity<Object>getAllDoctorsInClinic(Integer adminId) {

        Optional<Clinic> clinic = clinicRepository.findByClinicAdminId(adminId);

        if(clinic.isEmpty()) {
            return new ResponseEntity<>(
                    ResponseMessageDto.builder().message("Clinic not found").success(false).statusCode(404).build()
                    , HttpStatus.NOT_FOUND);
        }

        List<Doctor> doctors = clinicRepository.findAllDoctorsByClinicId(clinic.get().getClinicId());

        List<DoctorDTO> doctorDTOs = (List<DoctorDTO>) doctors.stream().map(doctor -> {
            return DoctorDTO.builder()
                    .doctorId(doctor.getId())
                    .firstName(doctor.getFirstName())
                    .lastName(doctor.getLastName())
                    .email(doctor.getEmail())
                    .phoneNumber(doctor.getPhoneNumber())
                    .build();
        }).toList();

        return new ResponseEntity<>(doctorDTOs, HttpStatus.OK);

    }
}