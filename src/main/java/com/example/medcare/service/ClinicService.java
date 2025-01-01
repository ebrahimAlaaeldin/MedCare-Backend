package com.example.medcare.service;


import com.example.medcare.Mappers.ClinicMapper;
import com.example.medcare.Mappers.DoctorMapper;
import com.example.medcare.dto.ClinicAdminDTO;
import com.example.medcare.dto.ClinicDTO;
import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.entities.Clinic;
import com.example.medcare.entities.Doctor;
import com.example.medcare.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClinicService {


    private final ClinicRepository clinicRepository;
    private final ClinicMapper clinicMapper;
    private final DoctorMapper doctorMapper;
    // get all clinics
    public ResponseEntity<Object> getAllClinics() {

        List<Clinic>  clinics= clinicRepository.findAllByIsVerifiedFalse();

        if (clinics == null)
            return new ResponseEntity<>(ResponseMessageDto.builder()
                    .message("No clinics Available")
                    .statusCode(404)
                    .success(false).build(), HttpStatus.NOT_FOUND);

        List<ClinicDTO> clinicsDtos = clinicMapper.toDTOs(clinics);

        return new ResponseEntity<>(clinicsDtos, HttpStatus.OK);
    }


    // get doctors in a clinic
    public ResponseEntity<Object> getDoctorsInClinic(Integer clinicId) {

        List<Doctor> doctors = clinicRepository.findAllDoctorsByClinicId(clinicId);

        if (doctors == null)
            return new ResponseEntity<>(ResponseMessageDto.builder()
                    .message("No doctors Available")
                    .statusCode(404)
                    .success(false).build(), HttpStatus.NOT_FOUND);

        List<DoctorDTO> doctorDtos = doctors.stream().map(doctorMapper::toDTO).collect(Collectors.toList());

        return new ResponseEntity<>(doctorDtos, HttpStatus.OK);
    }

    
}
