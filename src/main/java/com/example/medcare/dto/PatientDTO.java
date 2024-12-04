package com.example.medcare.dto;

import java.time.LocalDate;

import com.example.medcare.embedded.Address;
import com.example.medcare.Enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
public class PatientDTO {


    private String username;
    private String firstName;
    private String password;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Address address;
    private Role role;
    private Integer age;
    private LocalDate dateOfBirth; // Format example: 1999-12-31

    private String insuranceNumber;
    private String emergencyContactNumber;

}






    

