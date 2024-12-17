package com.example.medcare.dto;

import com.example.medcare.embedded.Address;
import com.example.medcare.embedded.ClinicPermit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ClinicDTO{

    private String clinicName;

     
    private ClinicAdminDTO clinicAdmin;

    private Address address;

    private ClinicPermit permit;
   
}
