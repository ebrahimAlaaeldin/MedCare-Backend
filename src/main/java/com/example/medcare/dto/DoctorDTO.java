package com.example.medcare.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO {

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String address;

    private String specialty;

    private List<String> ClinicNames;

    private String licenseNumber;

    private LocalDate issuingDate;
    
}
