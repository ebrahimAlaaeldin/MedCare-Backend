package com.example.medcare.dto;


import com.example.medcare.embedded.Address;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@Builder
public class SignUpRequest {

    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String role;
    private String phoneNumber;
    private Address address;
    @JsonFormat(pattern = "yyyy-dd-MM") // Specify the format expected
    private LocalDate dateOfBirth;
    //patient attributes
    private String insuranceNumber;
    private String emergencyContactNumber;

    //doctor attributes
    private String licenseNumber;
    private String specialty;
    private LocalDate issuingDate;





}
