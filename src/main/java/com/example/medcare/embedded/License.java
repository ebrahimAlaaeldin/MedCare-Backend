package com.example.medcare.embedded;


import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Embeddable
public class License {

    private String licenseNumber;
    private String Specialty;
    private String issuingDate;

}
