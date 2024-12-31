package com.example.medcare.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddDoctorDTO {
    private String doctorUsername;
    private int adminId;
}
