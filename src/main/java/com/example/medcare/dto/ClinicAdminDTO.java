package com.example.medcare.dto;

import java.time.LocalDate;

import com.example.medcare.embedded.Address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ClinicAdminDTO {


    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phoneNumber;
    private Address address;
    private LocalDate dateOfBirth;
    private String ClinicName;
    



}
