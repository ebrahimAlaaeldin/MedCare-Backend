package com.example.medcare.dto;

import java.time.LocalDate;


import com.example.medcare.Enums.Gender;
import com.example.medcare.embedded.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@AllArgsConstructor
@NoArgsConstructor

@SuperBuilder
public class DoctorDTO {

    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phoneNumber;
    private Address address;
    private Integer age;
    private LocalDate dateOfBirth;  // Format example: 1999-12-31
    // private List<String> ClinicNames;
    private String licenseNumber;
    private String Specialty;
    private LocalDate issuingDate;
    private Gender gender;
    
}
