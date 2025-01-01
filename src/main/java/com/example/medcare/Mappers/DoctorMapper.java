package com.example.medcare.Mappers;

import com.example.medcare.dto.DoctorDTO;
import com.example.medcare.entities.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

   public DoctorDTO toDTO(Doctor doctor){
       return DoctorDTO.builder()
               .doctorId(doctor.getId())
               .username(doctor.getUsername())
               .email(doctor.getEmail())
                .phoneNumber(doctor.getPhoneNumber())
                .address(doctor.getAddress())
                .age(doctor.getAge())
                .dateOfBirth(doctor.getBirthDate())
                .licenseNumber(doctor.getLicense().getLicenseNumber())
                .Specialty(doctor.getLicense().getSpecialty())
                .issuingDate(doctor.getLicense().getIssuingDate())
               .firstName(doctor.getFirstName())
               .lastName(doctor.getLastName())
               .build();
   }
}
